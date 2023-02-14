package trade.wayruha.mexc.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import okhttp3.Request;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.client.ApiClient;

import java.util.Set;
import java.util.stream.Collectors;

import static trade.wayruha.mexc.Constants.LISTEN_KEY_QUERY_PARAM;

public class WSClientFactory {
    public static final String PARTIAL_BOOK_CHANNEL_FORMAT = "spot@public.limit.depth.v3.api@%s@%s";
    public static final String USER_SPOT_ORDERS_CHANNEL_FORMAT = "spot@private.orders.v3.api";

    private final MexcConfig config;
    private final ApiClient apiClient;
    @Setter
    private ObjectMapper objectMapper;

    public WSClientFactory(MexcConfig config) {
        this(new ApiClient(config));
    }

    public WSClientFactory(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.config = apiClient.getConfig();
        this.objectMapper = new ObjectMapper();
    }

    public <T> WebSocketClient<T> orderBookSubscription(Set<String> symbols, int depth, WebSocketCallback<T> callback, Class<T> type) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(PARTIAL_BOOK_CHANNEL_FORMAT, sym, depth))
                .collect(Collectors.toSet());
        return new WebSocketClient<>(channels, callback, type, apiClient, objectMapper);
    }

    public <T> WebSocketClient<T> userSpotTradesSubscription(String listenKey, WebSocketCallback<T> callback, Class<T> type) {
        final Request request = new Request.Builder()
                .url(config.getWebSocketHost() + "?" + LISTEN_KEY_QUERY_PARAM + "=" + listenKey)
                .build();
        return userSpotTradesSubscription(request, callback, type);
    }

    public <T> WebSocketClient<T> userSpotTradesSubscription(WebSocketCallback<T> callback, Class<T> type) {
        return new AutoRenewalPrivateWSClient<>(Set.of(USER_SPOT_ORDERS_CHANNEL_FORMAT), callback, type, apiClient, objectMapper);
    }

    public <T> WebSocketClient<T> userSpotTradesSubscription(Request connectionRequest, WebSocketCallback<T> callback, Class<T> type) {
        return new WebSocketClient<>(connectionRequest, Set.of(USER_SPOT_ORDERS_CHANNEL_FORMAT), callback, type, apiClient, objectMapper);
    }
}
