package trade.wayruha.mexc.constant;

public class GlobalParams {
    public static int WS_CONNECTION_POOL_SIZE = 20;
    public static int WS_KEEP_ALIVE_DURATION_SEC = 300;
    public static int WS_CONNECT_TIMEOUT_MS = 300;
    public static int WS_READ_TIMEOUT_MS = 300;
    public static int WS_WRITE_TIMEOUT_MS = 300;

    public static int WEB_SOCKET_MAX_CHANNELS_PER_CONNECTION = 30;
    public static int WEB_SOCKET_RECONNECTION_DELAY_MS = 10_000;
    public static int WEB_SOCKET_KEEP_ALIVE_PERIOD_MIN = 45;
    public static int WEB_SOCKET_RESUBSCRIBE_PERIOD_MIN = 23 * 60;

}
