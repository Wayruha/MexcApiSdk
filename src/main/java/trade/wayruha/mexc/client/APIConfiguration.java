package trade.wayruha.mexc.client;

import lombok.Data;
import trade.wayruha.mexc.constant.APIConstants;

@Data
public class APIConfiguration {
    public static final String DEFAULT_ENDPOINT = "https://api.mexc.com/";

    private String endpoint;
    private String apiKey;
    private String apiSecret;
    private String passphrase;

    public APIConfiguration() {
        this(DEFAULT_ENDPOINT, null, null);
    }

    public APIConfiguration(String apiKey, String apiSecret) {
        this(DEFAULT_ENDPOINT, apiKey, apiSecret);
    }

    public APIConfiguration(String endpoint, String apiKey, String apiSecret) {
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
