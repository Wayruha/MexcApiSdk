package trade.wayruha.mexc.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import trade.wayruha.mexc.MexcApiException;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.client.helper.HttpClientBuilder;
import trade.wayruha.mexc.client.helper.RetrofitBuilder;
import trade.wayruha.mexc.dto.MexcResponse;

import java.io.IOException;

@Slf4j
public class ApiClient {
    @Getter
    private final MexcConfig config;
    private final OkHttpClient httpClient;
    private final Retrofit retrofit;

    public ApiClient(MexcConfig config) {
        this(config, new HttpClientBuilder(config));
    }

    public ApiClient(MexcConfig config, HttpClientBuilder httpClientBuilder) {
        this.config = config;
        this.httpClient = httpClientBuilder.buildClient();
        this.retrofit = RetrofitBuilder.buildRetrofit(config, this.httpClient);
    }

    public <T> T createService(final Class<T> service) {
        return this.retrofit.create(service);
    }

    public <T> MexcResponse<T> executeSync(Call<T> call) {
        try {
            final Response<T> response = call.execute();
            final T body = response.body();
            if (response.isSuccessful()) {
                return new MexcResponse<>(response.code(), body);
            } else {
                log.error("Request failed: {} ", response);
                throw new MexcApiException(response.code(), response.message());
            }
        } catch (IOException e) {
            log.error("Request failed: {} ", call.request(), e);
            throw new MexcApiException(e.getMessage(), e);
        }
    }

    public WebSocket createWebSocket(Request request, WebSocketListener listener) {
        return httpClient.newWebSocket(request, listener);
    }
}
