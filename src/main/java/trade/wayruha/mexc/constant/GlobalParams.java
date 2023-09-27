package trade.wayruha.mexc.constant;

public class GlobalParams {
    /**
     * Decorator to indicate that an endpoint requires signature
     */
    public static final String ENDPOINT_SECURITY_SIGNED = "API_SIGNED";
    public static final String ENDPOINT_SECURITY_SIGNED_HEADER = ENDPOINT_SECURITY_SIGNED + ": #";
    /**
     * The default timeout is 30 seconds.
     */
    public static final long DEFAULT_CONNECTION_TIMEOUT = 30 * 1000;
    public static final String LISTEN_KEY_QUERY_PARAM = "listenKey";
    public static final long DEFAULT_CONNECTION_TTL = 10 * 1000;
    public static int WEB_SOCKET_MAX_CHANNELS_PER_CONNECTION = 30;
    public static long WEB_SOCKET_RECONNECTION_DELAY_MS = 10_000;
    public static int WEB_SOCKET_CHANNEL_KEEP_ALIVE_PERIOD_SEC = 55;
    public static int WEB_SOCKET_KEEP_ALIVE_PERIOD_MIN = 45;
    public static int WEB_SOCKET_PRIVATE_RESUBSCRIBE_PERIOD_MIN = 23 * 60;
    public static int WEB_SOCKET_RECONNECT_ATTEMPT_NUMBER = 5;
}
