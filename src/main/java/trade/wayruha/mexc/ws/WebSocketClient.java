package trade.wayruha.mexc.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.utils.IdGenerator;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static trade.wayruha.mexc.constant.GlobalParams.WEB_SOCKET_MAX_CHANNELS_PER_CONNECTION;

@Slf4j
public class WebSocketClient<T> extends WebSocketListener {
    protected final MexcConfig config;
    protected final ApiClient apiClient;
    protected final WebSocketCallback<T> callback;
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

    WebSocketClient(Set<String> channels, WebSocketCallback<T> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        this(buildRequestFromHost(apiClient.getConfig().getWebSocketHost()), channels, listener, type, apiClient, mapper);
    }

    WebSocketClient(Request connectionRequest, Set<String> channels, WebSocketCallback<T> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
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

    protected void connect(Set<String> channels) {
        if (this.state != WSState.CONNECTED && this.state != WSState.CONNECTING) {
            log.debug("{} Connecting...", logPrefix);
            this.state = WSState.CONNECTING;
            this.webSocket = apiClient.createWebSocket(this.connectionRequest, this);
        } else {
            log.warn("{} Already connected", logPrefix);
        }
        if (!channels.isEmpty()) subscribe(channels);
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
        send(content);
        this.channels.addAll(newChannels);
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
        channels.clear();
    }

    public void reConnect() {
        close();
        connect(channels);
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
            final T obj = objectMapper.readValue(text, type);
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
        super.onFailure(webSocket, t, response);
        closeOnError(t);
        callback.onFailure(t, response);
    }

    public long getLastReceivedTime() {
        return this.lastReceivedTime;
    }

    private void closeOnError(Throwable ex) {
        log.error("{} [Connection error] Connection is closing due to error: {}", logPrefix, ex != null ? ex.getMessage() : null);
        close();
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
        SUBSCRIPTION, UNSUBSCRIPTION, PING;
    }
}
