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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;
import static trade.wayruha.mexc.constant.GlobalParams.*;
import static trade.wayruha.mexc.constant.Messages.WEBSOCKET_INTERRUPTED_EXCEPTION;

@Slf4j
public class WebSocketClient<T> extends WebSocketListener {
    protected final MexcConfig config;
    protected final ApiClient apiClient;
    protected final WebSocketCallback<MexcWSResponse<T>> callback;
    @Getter
    protected final Set<String> channels;
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
    private final AtomicInteger reconnectAttemptNum = new AtomicInteger(0);
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledPingTask;

    WebSocketClient(Set<String> channels, WebSocketCallback<MexcWSResponse<T>> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        this(buildRequestFromHost(apiClient.getConfig().getWebSocketHost()), channels, listener, type, apiClient, mapper);
    }

    WebSocketClient(Request connectionRequest, Set<String> channels, WebSocketCallback<MexcWSResponse<T>> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        this.id = IdGenerator.getNextId();
        this.logPrefix = "[ws-" + this.id + "]";
        this.channels = new HashSet<>();
        this.type = type;
        this.callback = listener;
        this.apiClient = apiClient;
        this.config = apiClient.getConfig();
        this.objectMapper = mapper;
        this.connectionRequest = connectionRequest;
        connect(channels);
    }

    protected void connect(Set<String> newChannels) {
        if (this.state != WSState.CONNECTED && this.state != WSState.CONNECTING) {
            log.debug("{} Connecting...", logPrefix);
            this.state = WSState.CONNECTING;
            this.webSocket = apiClient.createWebSocket(this.connectionRequest, this);
        } else {
            log.warn("{} Already connected", logPrefix);
        }
        if (!newChannels.isEmpty()) {
            this.subscribe(newChannels);
            this.scheduledPingTask = this.getScheduler().scheduleAtFixedRate(new PingTask(newChannels),
                    WEB_SOCKET_KEEP_ALIVE_TOPIC_PERIOD_SEC, WEB_SOCKET_KEEP_ALIVE_TOPIC_PERIOD_SEC, TimeUnit.SECONDS);
        }
    }
    private ScheduledExecutorService getScheduler(){
        if(isNull(this.scheduler)){
            this.scheduler = Executors.newSingleThreadScheduledExecutor();
        }
        return this.scheduler;
    }

    public void send(String str) {
        boolean result = false;
        log.debug("{}:send() {}", logPrefix, str);
        if (webSocket != null) {
            result = webSocket.send(str);
        }
        if (!result) {
            log.error("{}: Failed to send message: {}", logPrefix, str);
            closeOnError(null);
        }
    }

    @SneakyThrows
    public void subscribe(Collection<String> newChannels) {
        if (this.channels.size() + newChannels.size() > WEB_SOCKET_MAX_CHANNELS_PER_CONNECTION)
            throw new IllegalArgumentException("Can't subscribe to more then " + WEB_SOCKET_MAX_CHANNELS_PER_CONNECTION + "channels in single WS connection");
        final Message msg = new Message(Action.SUBSCRIPTION, newChannels);
        final String content = objectMapper.writeValueAsString(msg);
        this.send(content);
        this.channels.addAll(newChannels);
    }

    @SneakyThrows
    public void ping(Collection<String> channels) {
        final Message msg = new Message(Action.PING, channels);
        final String content = objectMapper.writeValueAsString(msg);
        this.send(content);
        log.info("{} ping was send to channels {} ", logPrefix, channels);
    }

    @SneakyThrows
    public void unsubscribe(Collection<String> channels) {
        final Message msg = new Message(Action.UNSUBSCRIPTION, channels);
        final String content = objectMapper.writeValueAsString(msg);
        send(content);
        this.channels.removeAll(channels);
    }

    public void close() {
        log.info("{} closing", logPrefix);
        if (webSocket != null) {
            webSocket.cancel();
            webSocket = null;
        }
        state = WSState.IDLE;
        this.channels.clear();
        scheduledPingTask.cancel(false);
    }

    public void reConnect() {
        this.close();
        this.connect(this.channels);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.debug("{} connected", log);
        state = WSState.CONNECTED;
        lastReceivedTime = System.currentTimeMillis();
        this.webSocket = webSocket;
        callback.onOpen(response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        lastReceivedTime = System.currentTimeMillis();
        log.trace("{} onMessage: {}", logPrefix, text);
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
        super.onClosed(webSocket, code, reason);
        if (state == WSState.CONNECTED || state == WSState.CONNECTING) {
            state = WSState.IDLE;
        }
        log.debug("{} closed", logPrefix);
        callback.onClosed(code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        log.warn("{} [Connection error] Connection was interrupted due to error on webSocket: {}, cause {}, response {}", logPrefix,
                 webSocket, t.getMessage(), response);
        var attemptNum = reconnectAttemptNum.get();
        if(attemptNum < WEB_SOCKET_RECONNECT_ATTEMPT_NUMBER) {
            reconnectAttemptNum.set(++attemptNum);
            reConnect();
        }else {
            super.onFailure(webSocket, t, response);
            closeOnError(t);
            callback.onFailure(t, response);
        }
    }

    public long getLastReceivedTime() {
        return this.lastReceivedTime;
    }

    private void closeOnError(Throwable ex) {
        log.warn("{} [Connection error] Connection is closing due to error: {}", logPrefix,
                ex != null ? ex.getMessage() : WEBSOCKET_INTERRUPTED_EXCEPTION);
        this.close();
        state = WSState.CLOSED_ON_ERROR;
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
       private final Collection<String> existedChannels;

       public PingTask(Collection<String> existedChannels) {
           this.existedChannels = existedChannels;
       }
        @SneakyThrows
        @Override
        public void run() {
            try {
                ping(existedChannels);
            } catch (Exception ex) {
                log.error("{} Re-subscription error. Try again in {} sec...", logPrefix, WEB_SOCKET_RECONNECTION_DELAY_MS / 1000);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
                reConnect();
            }
        }
    }
}
