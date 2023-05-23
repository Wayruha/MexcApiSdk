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
import trade.wayruha.mexc.MexcResponse;
import trade.wayruha.mexc.client.helper.HttpClientBuilder;
import trade.wayruha.mexc.client.helper.RetrofitBuilder;

import java.io.IOException;

import static java.util.Objects.nonNull;
import static trade.wayruha.mexc.constant.Messages.API_CLIENT_ERROR_MESSAGE_PARSE_EXCEPTION;

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
        String rawRequestData = call.request().toString();
        try {
            final Response<T> response = call.execute();
            final T body = response.body();
            if (response.isSuccessful()) {
                return new MexcResponse<>(response.code(), body);
            }
            String errorMessage = nonNull(response.errorBody()) ? response.errorBody().string() : API_CLIENT_ERROR_MESSAGE_PARSE_EXCEPTION;
            log.error("Request failed. Request data: {}. Response error message: {} ", rawRequestData, errorMessage);
            throw new MexcApiException(response.code(), errorMessage);
        } catch (IOException e) {
            log.error("Request failed. Request data: {},  response: {} ", rawRequestData, call.request(), e);
            throw new MexcApiException(e.getMessage(), e);
        }
    }

    public WebSocket createWebSocket(Request request, WebSocketListener listener) {
        return httpClient.newWebSocket(request, listener);
    }
}
