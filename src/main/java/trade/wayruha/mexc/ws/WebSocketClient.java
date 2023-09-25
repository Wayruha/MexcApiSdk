package trade.wayruha.mexc.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.MexcWSResponse;
import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.enums.WSState;
import trade.wayruha.mexc.utils.IdGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static trade.wayruha.mexc.constant.GlobalParams.WEB_SOCKET_RECONNECTION_DELAY_MS;
import static trade.wayruha.mexc.constant.Messages.WEBSOCKET_INTERRUPTED_EXCEPTION;

@Slf4j
public class WebSocketClient<T> extends WebSocketListener {
    protected final MexcConfig config;
    protected final ApiClient apiClient;
    protected final WebSocketCallback<MexcWSResponse<T>> callback;
    @Getter
    protected final Set<String> channels;
    @Getter
    protected final Set<String> failedChannels;
    @Getter
    protected final int id;
    protected final ObjectMapper objectMapper;
    protected final String logPrefix;
    protected final Class<T> type;
    protected Request connectionRequest;

    @Getter
    private WSState state;
    private WebSocket webSocket;
    private long lastReceivedTime;
    private final AtomicInteger currentReconnectAttemptIndex = new AtomicInteger(0);
    protected ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledPingTask;

    WebSocketClient(Set<String> channels, WebSocketCallback<MexcWSResponse<T>> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        this(buildRequestFromHost(apiClient.getConfig().getWebSocketHost()), channels, listener, type, apiClient,
                mapper);
    }

    WebSocketClient(Request connectionRequest, Set<String> channels, WebSocketCallback<MexcWSResponse<T>> listener,
                    Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        this.id = IdGenerator.getNextId();
        this.logPrefix = "[ws-" + this.id + "]";
        this.channels = Collections.synchronizedSet(new HashSet<>());
        this.failedChannels = Collections.synchronizedSet(new HashSet<>());
        this.type = type;
        this.callback = listener;
        this.apiClient = apiClient;
        this.config = apiClient.getConfig();
        this.objectMapper = mapper;
        this.connectionRequest = connectionRequest;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        connect(channels);
        //create ping task for keep alive WS connection if no messages were produced be server
        this.scheduledPingTask = scheduler.scheduleAtFixedRate(new PingTask(),
                this.config.getWebSocketChannelKeepAlivePeriodSec(), this.config.getWebSocketChannelKeepAlivePeriodSec(), TimeUnit.SECONDS);
    }

    protected void connect(Set<String> newChannels) {
        if (newChannels.isEmpty()) {
            log.info("{} Attempt to connect to empty channels list will be skipped.", logPrefix);
            return;
        }
        if (this.state != WSState.CONNECTED && this.state != WSState.CONNECTING) {
            log.debug("{} Start connecting to channels: {} ...", logPrefix, newChannels);
            this.state = WSState.CONNECTING;
            this.webSocket = apiClient.createWebSocket(this.connectionRequest, this);
            if (this.webSocket == null) {
                log.error("Error connect {}", this.connectionRequest);
            }
        } else {
            log.warn("{} WS is already opened and connected to channels: {}", logPrefix, channels);
        }
        if (isNotEmpty(newChannels)) {
            this.subscribe(newChannels);
        }
    }

    public void sendRequest(String request) {
        boolean result = false;
        log.debug("{} Try to send request {}", logPrefix, request);
        if (webSocket != null) {
            result = webSocket.send(request);
        }
        if (!result) {
            log.error("{} Failed to send message: {}", logPrefix, request);
            closeOnError(null);
        }
    }

    @SneakyThrows
    public void subscribe(Collection<String> newChannels) {
        log.trace("{} Compose connection request for channels: {}", logPrefix, newChannels.toString());
        if (this.channels.size() + newChannels.size() > this.config.getWebSocketMaxChannelsPerConnection()) {
            throw new IllegalArgumentException("Can't subscribe to more then " + this.config.getWebSocketMaxChannelsPerConnection() + " channels in single WS connection");
        }
        final Message msg = new Message(Action.SUBSCRIPTION, newChannels);
        final String content = objectMapper.writeValueAsString(msg);
        this.sendRequest(content);
        this.channels.addAll(newChannels);
    }

    @SneakyThrows
    public void ping(Collection<String> channels) {
        final Message msg = new Message(Action.PING, channels);
        final String content = objectMapper.writeValueAsString(msg);
        this.sendRequest(content);
        log.trace("{} Ping was send to channels {} ", logPrefix, channels);
    }

    @SneakyThrows
    public void unsubscribe(Collection<String> deletedChannels) {
        final Message msg = new Message(Action.UNSUBSCRIPTION, deletedChannels);
        final String content = objectMapper.writeValueAsString(msg);
        sendRequest(content);
        this.channels.removeAll(deletedChannels);
    }

    public void close() {
        log.debug("{} Closing WS channels: {}", logPrefix, this.channels);
        state = WSState.IDLE;
        if (webSocket != null) {
            webSocket.cancel();
            webSocket = null;
        }
        this.channels.clear();
    }

    public boolean reConnect() {
        if (!this.failedChannels.isEmpty()) {
            log.trace("{} already reConnecting to channels: {}", logPrefix, this.failedChannels);
            return true;
        }
        this.failedChannels.addAll(this.channels);
        while (!this.failedChannels.isEmpty() &&
                (this.config.isWebSocketReconnectAlways() || currentReconnectAttemptIndex.get() < this.config.getWebSocketMaxReconnectAttemptNumber())) {
            try {
                currentReconnectAttemptIndex.incrementAndGet();
                log.debug("{} Try to reconnect to WebSocket channels: {}. Attempt #{}",
                        logPrefix, this.failedChannels, currentReconnectAttemptIndex.get());
                this.close();
                this.connect(this.failedChannels);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
            } catch (Exception e) {
                log.error("{} [Connection error] Error to reconnect {}", logPrefix, e.getMessage(), e);
                try {
                    Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
                } catch (InterruptedException ex) {
                    log.error("{} [Connection error] Error to sleep until next reconnection attempt {}. Skip sleeping", logPrefix, ex.getMessage(), ex);
                }
            }
        }
        if (isNotEmpty(failedChannels)) {
            log.warn("{} [Connection error] After {} attempts we couldn't reconnect to WebSocket for channels: {}.",
                    logPrefix, this.config.getWebSocketMaxReconnectAttemptNumber(), failedChannels);
            failedChannels.clear();
            return false;
        }

        return true;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        //clear failed channels due to successful connection
        this.failedChannels.clear();
        //reset reconnection indexer due to successful connection
        currentReconnectAttemptIndex.set(0);
        log.debug("{} onOpen WS event: Connected to channels {}", logPrefix, this.channels);
        state = WSState.CONNECTED;
        lastReceivedTime = System.currentTimeMillis();
        this.webSocket = webSocket;
        callback.onOpen(response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        lastReceivedTime = System.currentTimeMillis();
        log.trace("{} onMessage WS event: {}", logPrefix, text);
        try {
            JavaType typeMap = objectMapper.getTypeFactory().constructParametricType(MexcWSResponse.class, type);
            MexcWSResponse<T> obj = objectMapper.readValue(text, typeMap);
            callback.onResponse(obj);
        } catch (JsonProcessingException e) {
            log.error("{} Deserialization error {} for {}", log, e.getMessage(), text);
            closeOnError(e);
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        log.trace("{} onClosed WS event: {}, code: {}", logPrefix, reason, code);
        super.onClosed(webSocket, code, reason);
        if (state == WSState.CONNECTED || state == WSState.CONNECTING) {
            state = WSState.IDLE;
        }
        log.debug("{} Closed", logPrefix);
        callback.onClosed(code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (state == WSState.IDLE) {
            return;//this is a handled websocket closure. No failure event should be created.
        }
        log.warn("{} onFailure WS event cause: {}", logPrefix, t.getMessage());
        if (this.reConnect()) {//try to reconnect
            log.trace("{} [Connection error] Reconnection to WebSocket channels: {} was initiated.",
                    logPrefix, this.channels);
        } else {
            log.warn("{} [Connection error] After {} attempts we couldn't reconnect to WebSocket for all channels: {}.",
                    logPrefix, this.config.getWebSocketMaxReconnectAttemptNumber(), this.channels);
            this.failedChannels.clear();
            super.onFailure(webSocket, t, response);
            closeOnError(t);
            callback.onFailure(t, response);
        }
    }

    public long getLastReceivedTime() {
        return this.lastReceivedTime;
    }

    private void closeOnError(Throwable ex) {
        log.warn("{} [Connection error] Connection will be closed due to error: {}", logPrefix,
                ex != null ? ex.getMessage() : WEBSOCKET_INTERRUPTED_EXCEPTION);
        this.close();
        state = WSState.CLOSED_ON_ERROR;
        scheduledPingTask.cancel(false);
    }

    static Request buildRequestFromHost(String host) {
        return new Request.Builder().url(host).build();
    }

    @AllArgsConstructor
    @Getter
    static class Message {
        private Action method;
        private Collection<String> params;
    }

    enum Action {
        SUBSCRIPTION, UNSUBSCRIPTION, PING
    }

    class PingTask implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            try {
                if (isNotEmpty(channels)) {
                    ping(channels);
                }
            } catch (Exception ex) {
                log.error("{} Ping error. Try to reconnect and send again in {} sec...", logPrefix, WEB_SOCKET_RECONNECTION_DELAY_MS / 1000);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
                reConnect();
            }
        }
    }
}
