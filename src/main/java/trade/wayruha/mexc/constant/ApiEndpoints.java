package trade.wayruha.mexc.constant;

public class ApiEndpoints {

    //<editor-fold desc="Public WebSocket streams endpoints">
    /**
     * Partial Book Depth Streams.
     *
     * @apiNote e.g. request: spot@public.limit.depth.v3.api@BTCUSDT@<level>
     */
    public static final String PARTIAL_BOOK_CHANNEL_FORMAT = "spot@public.limit.depth.v3.api@%s@%s";

    /**
     * Trade Streams
     *
     * @apiNote e.g. request: spot@public.deals.v3.api@BTCUSDT
     */
    public static final String TRADE_CHANNEL_FORMAT = "spot@public.deals.v3.api@%s";

    /**
     * The Kline/Candlestick Stream
     *
     * @apiNote e.g. request: spot@public.kline.v3.api@BTCUSDT@Min1
     */
    public static final String CANDLESTICK_CHANNEL_FORMAT = "spot@public.kline.v3.api@%s@%s";

    /**
     * Diff.Depth Stream
     *
     * @apiNote e.g. request: spot@public.increase.depth.v3.api@BTCUSDT
     */
    public static final String DIFF_DEPTH_CHANNEL_FORMAT = "spot@public.increase.depth.v3.api@%s";

    /**
     * Individual Symbol Book Ticker Stream
     *
     * @apiNote e.g. request: spot@public.bookTicker.v3.api@BTCUSDT
     */
    public static final String INDIVIDUAL_SYMBOL_BOOK_CHANNEL_FORMAT = "spot@public.bookTicker.v3.api@%s";
    //</editor-fold>


    //<editor-fold desc="Private WebSocket streams endpoint">
    /**
     * Spot Account Orders Stream
     */
    public static final String USER_SPOT_ORDERS_CHANNEL_FORMAT = "spot@private.orders.v3.api";

    /**
     * Spot Account Deals Stream
     */
    public static final String USER_SPOT_DEALS_CHANNEL_FORMAT = "spot@private.deals.v3.api";

    /**
     * Margin Account Orders Stream
     */
    public static final String USER_MARGIN_ORDERS_CHANNEL_FORMAT = "margin@private.orders.v3.api";

    /**
     * Margin Account Risk Rate Stream
     *
     * @apiNote e.g. request: margin@private.risk.rate.v3.api@BTCUSDT
     */
    public static final String USER_MARGIN_RISK_RATE_CHANNEL_FORMAT = "margin@private.risk.rate.v3.api@%s";
    //</editor-fold>
}
