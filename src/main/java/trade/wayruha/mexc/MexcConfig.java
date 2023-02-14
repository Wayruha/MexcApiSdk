package trade.wayruha.mexc;

import lombok.Data;
import trade.wayruha.mexc.constant.APIConstants;

@Data
public class MexcConfig {
    public static final String DEFAULT_HTTP_HOST = "https://api.mexc.com/";
    public static final String DEFAULT_WS_HOST = "wss://wbs.mexc.com/ws";

    private String endpoint;
    private String apiKey;
    private String apiSecret;
    private String passphrase;
    private String httpHost = DEFAULT_HTTP_HOST;
    private String webSocketHost = DEFAULT_WS_HOST;

    public MexcConfig() {
        this(DEFAULT_HTTP_HOST, null, null);
    }

    public MexcConfig(String apiKey, String apiSecret) {
        this(DEFAULT_HTTP_HOST, apiKey, apiSecret);
    }

    public MexcConfig(String endpoint, String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.passphrase = null;
        this.endpoint = endpoint;
        this.connectTimeout = APIConstants.TIMEOUT;
        this.readTimeout = APIConstants.TIMEOUT;
        this.writeTimeout = APIConstants.TIMEOUT;
        this.retryOnConnectionFailure = true;
        this.print = false;
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
