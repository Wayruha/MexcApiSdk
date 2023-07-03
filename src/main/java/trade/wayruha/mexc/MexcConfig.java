package trade.wayruha.mexc;

import lombok.Data;
import trade.wayruha.mexc.constant.GlobalParams;

import static java.util.Objects.nonNull;

@Data
public class MexcConfig {
    public static final String DEFAULT_HTTP_HOST = "https://api.mexc.com/";
    public static final String DEFAULT_WS_HOST = "wss://wbs.mexc.com/ws";

    private String apiKey;
    private String apiSecret;
    private String passphrase;
    private String httpHost;
    private long recvWindow;
    private String webSocketHost;
    private boolean webSocketReconnectAlways;
    private int webSocketMaxReconnectAttemptNumber;
    private int webSocketChannelKeepAlivePeriodSec;

    public MexcConfig() {
        this(DEFAULT_HTTP_HOST, DEFAULT_WS_HOST, "", "", null, null, null);
    }

    public MexcConfig(String apiKey, String apiSecret) {
        this(DEFAULT_HTTP_HOST, DEFAULT_WS_HOST, apiKey, apiSecret, null, null, null);
    }

    public MexcConfig(String httpHost, String webSocketHost, String apiKey, String apiSecret) {
        this(httpHost, webSocketHost, apiKey, apiSecret, null, null, null);
    }

    public MexcConfig(String apiKey, String apiSecret, Boolean webSocketReconnectAlways, Integer webSocketMaxReconnectAttemptNumber) {
        this(DEFAULT_HTTP_HOST, DEFAULT_WS_HOST, apiKey, apiSecret, null, webSocketReconnectAlways, webSocketMaxReconnectAttemptNumber);
    }

    public MexcConfig(String httpHost, String webSocketHost, String apiKey, String apiSecret, Long connectTimeoutDefault,
                      Boolean webSocketReconnectAlways, Integer webSocketMaxReconnectAttemptNumber) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.passphrase = null;
        this.httpHost = httpHost;
        this.webSocketHost = webSocketHost;
        this.connectTimeout = GlobalParams.DEFAULT_CONNECTION_TIMEOUT;
        this.readTimeout = GlobalParams.DEFAULT_CONNECTION_TIMEOUT;
        this.writeTimeout = GlobalParams.DEFAULT_CONNECTION_TIMEOUT;
        this.retryOnConnectionFailure = true;
        this.print = false;
        this.recvWindow = nonNull(connectTimeoutDefault) ? connectTimeoutDefault : GlobalParams.DEFAULT_CONNECTION_TTL;
        this.webSocketReconnectAlways = nonNull(webSocketReconnectAlways) ? webSocketReconnectAlways : false;
        this.webSocketMaxReconnectAttemptNumber = nonNull(webSocketMaxReconnectAttemptNumber) ? webSocketMaxReconnectAttemptNumber : GlobalParams.WEB_SOCKET_RECONNECT_ATTEMPT_NUMBER;
        this.webSocketChannelKeepAlivePeriodSec = GlobalParams.WEB_SOCKET_CHANNEL_KEEP_ALIVE_PERIOD_SEC;
    }

    /**
     * Host connection timeout.
     */
    private long connectTimeout;
    /**
     * The host reads the information timeout.
     */
    private long readTimeout;
    /**
     * The host writes the information timeout.
     */
    private long writeTimeout;
    /**
     * Failure reconnection, default true.
     */
    private boolean retryOnConnectionFailure;

    /**
     * Should we log request's data?
     */
    private boolean print;
}
