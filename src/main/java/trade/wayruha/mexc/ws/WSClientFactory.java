package trade.wayruha.mexc.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import rx.annotations.Beta;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.MexcWSResponse;
import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.dto.*;
import trade.wayruha.mexc.enums.Interval;
import trade.wayruha.mexc.enums.OrderBookDepth;

import java.util.Set;
import java.util.stream.Collectors;

import static trade.wayruha.mexc.constant.ApiEndpoints.*;

@SuppressWarnings("unused")
public class WSClientFactory {

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

    //Public methods

    /**
     * Subscription on Partial Book Depth Stream
     *
     * @return OrderBook with Top bids and asks for specified limit
     */
    public WebSocketClient<OrderBook> orderBookSubscription(Set<String> symbols, OrderBookDepth orderBookDepth,
                                                            WebSocketCallback<MexcWSResponse<OrderBook>> callback) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(PARTIAL_BOOK_CHANNEL_FORMAT, sym, orderBookDepth.getDepth()))
                .collect(Collectors.toSet());
        return new WebSocketClient<>(channels, callback, OrderBook.class, apiClient, objectMapper);
    }

    /**
     * Subscription on Trade Streams, that push raw trade information.
     *
     * @return finished Deals with a unique buyer and seller.
     */
    public WebSocketClient<Trades> tradesSubscription(Set<String> symbols, WebSocketCallback<MexcWSResponse<Trades>> callback) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(TRADE_CHANNEL_FORMAT, sym))
                .collect(Collectors.toSet());
        return new WebSocketClient<>(channels, callback, Trades.class, apiClient, objectMapper);
    }

    /**
     * Subscription on The Kline/Candlestick Stream push updates to the current klines/candlestick every second.
     *
     * @return Ticker data for specified interval
     * @implNote Use Ticker.class to map response
     */
    @Beta
    public <T> WebSocketClient<T> candlestickSubscription(Set<String> symbols, Interval interval, WebSocketCallback<MexcWSResponse<T>> callback, Class<T> type) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(CANDLESTICK_CHANNEL_FORMAT, sym, interval))
                .collect(Collectors.toSet());
        return new WebSocketClient<>(channels, callback, type, apiClient, objectMapper);
    }

    /**
     * Subscription on Diff.Depth Stream
     * If the quantity is 0, it means that the order of the price has been cancel or traded,remove the price level.
     *
     * @return OrderBook with last updates of placed orders
     */
    public WebSocketClient<OrderBook> diffDepthSubscription(Set<String> symbols, WebSocketCallback<MexcWSResponse<OrderBook>> callback) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(DIFF_DEPTH_CHANNEL_FORMAT, sym))
                .collect(Collectors.toSet());
        return new WebSocketClient<>(channels, callback, OrderBook.class, apiClient, objectMapper);
    }

    /**
     * Subscription on Individual Symbol Book Ticker Stream, that pushes any update to
     * the best bid or ask's price or quantity in real-time for a specified symbol.
     *
     * @return Best price/qty on the order book for a symbol or symbols.
     * @implNote Use BookTicker.class to map response
     */
    @Beta
    public <T> WebSocketClient<T> individualSymbolSubscription(Set<String> symbols, WebSocketCallback<MexcWSResponse<T>> callback, Class<T> type) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(INDIVIDUAL_SYMBOL_BOOK_CHANNEL_FORMAT, sym))
                .collect(Collectors.toSet());
        return new WebSocketClient<>(channels, callback, type, apiClient, objectMapper);
    }

    //region User WebSocket methods
    /**
     * Subscription on Spot Account (Limit/Market) Orders Stream
     */
    public WebSocketClient<Trade> userSpotDealsSubscription(WebSocketCallback<MexcWSResponse<Trade>> callback) {
        return new AutoRenewalPrivateWSClient<>(Set.of(USER_SPOT_DEALS_CHANNEL_FORMAT), callback, Trade.class, apiClient, objectMapper);
    }

    /**
     * Subscription on Margin Account Orders Stream
     *
     * @implNote Use Deal.class to map response
     */
    @Beta
    public <T> WebSocketClient<T> userMarginOrdersSubscription(WebSocketCallback<MexcWSResponse<T>> callback, Class<T> type) {
        return new AutoRenewalPrivateWSClient<>(Set.of(USER_MARGIN_ORDERS_CHANNEL_FORMAT), callback, type, apiClient, objectMapper);
    }

    /**
     * Subscription on  Margin Account Risk Rate Stream
     *
     * @implNote Use Deal.class to map response
     */
    public WebSocketClient<AccountAssetBalance> userAccountAssetsSubscription(WebSocketCallback<MexcWSResponse<AccountAssetBalance>> callback) {
        return new AutoRenewalPrivateWSClient<>(Set.of(USER_ACCOUNT_ASSETS_CHANNEL_FORMAT), callback, AccountAssetBalance.class, apiClient, objectMapper);
    }

    @Beta
    public <T> WebSocketClient<T> userMarginRiskRateSubscription(Set<String> symbols, WebSocketCallback<MexcWSResponse<T>> callback, Class<T> type) {
        final Set<String> channels = symbols.stream()
                .map(sym -> String.format(USER_MARGIN_RISK_RATE_CHANNEL_FORMAT, sym))
                .collect(Collectors.toSet());
        return new AutoRenewalPrivateWSClient<>(channels, callback, type, apiClient, objectMapper);
    }

    /**
     * Subscription on spot (Limit/Market) orders stream for your Account.
     * Stream push all updates to users orders on a symbol
     */
    public WebSocketClient<Order> userSpotOrdersSubscription(WebSocketCallback<MexcWSResponse<Order>> callback) {
        return new AutoRenewalPrivateWSClient<>(Set.of(USER_SPOT_ORDERS_CHANNEL_FORMAT), callback, Order.class, apiClient, objectMapper);
    }
    //endregion

}
