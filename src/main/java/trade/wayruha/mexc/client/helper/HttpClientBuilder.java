package trade.wayruha.mexc.client.helper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.security.SignatureInterceptor;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class HttpClientBuilder {

    private final MexcConfig config;
    private final HttpLoggingInterceptor loggingInterceptor;

    public HttpClientBuilder(final MexcConfig config) {
        this.config = config;
        this.loggingInterceptor = null;
    }

    public HttpClientBuilder(final MexcConfig config, HttpLoggingInterceptor loggingInterceptor) {
        this.config = config;
        this.loggingInterceptor = loggingInterceptor;
    }

    public OkHttpClient buildClient() {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(this.config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(this.config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(this.config.getWriteTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(this.config.isRetryOnConnectionFailure());
        if (this.config.isPrint() && loggingInterceptor != null) {
            clientBuilder.addInterceptor(loggingInterceptor);
        }
        if(isNotEmpty(config.getApiKey())) {
            final SignatureInterceptor signatureInterceptor = new SignatureInterceptor(config.getApiKey(), config.getApiSecret(), config.getRecvWindow());
            clientBuilder.addInterceptor(signatureInterceptor);
        }
        return clientBuilder.build();
    }
}
