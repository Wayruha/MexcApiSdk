package trade.wayruha.mexc.client;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class HttpClientBuilder {

    private final APIConfiguration config;
    private final HttpLoggingInterceptor loggingInterceptor;

    public HttpClientBuilder(final APIConfiguration config) {
        this.config = config;
        this.loggingInterceptor = null;
    }

    public HttpClientBuilder(final APIConfiguration config, HttpLoggingInterceptor loggingInterceptor) {
        this.config = config;
        this.loggingInterceptor = loggingInterceptor;
    }

    public OkHttpClient buildClient() {
        final OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        final SignatureInterceptor signatureInterceptor = new SignatureInterceptor(config.getApiKey(), config.getApiSecret());
        clientBuilder.connectTimeout(this.config.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(this.config.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(this.config.getWriteTimeout(), TimeUnit.SECONDS)
                .retryOnConnectionFailure(this.config.isRetryOnConnectionFailure());
        if (this.config.isPrint() && loggingInterceptor != null) {
            clientBuilder.addInterceptor(loggingInterceptor);
        }
        clientBuilder.addInterceptor(signatureInterceptor);
        return clientBuilder.build();
    }
}
