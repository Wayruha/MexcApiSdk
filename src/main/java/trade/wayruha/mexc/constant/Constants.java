package trade.wayruha.mexc.constant;

public class Constants {
    /**
     * Decorator to indicate that an endpoint requires signature
     */
    public static final String ENDPOINT_SECURITY_SIGNED = "API_SIGNED";
    public static final String ENDPOINT_SECURITY_SIGNED_HEADER = ENDPOINT_SECURITY_SIGNED + ": #";

    public static final int DEFAULT_RECEIVING_WINDOW = 60_000;

    public static final String LISTEN_KEY_QUERY_PARAM = "listenKey";
}
