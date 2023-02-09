package trade.wayruha.mexc.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import trade.wayruha.mexc.MexcApiException;

import java.io.IOException;

@Slf4j
public class ApiClient {
    private final APIConfiguration config;
    private OkHttpClient httpClient;
    private final Retrofit retrofit;

    public ApiClient(APIConfiguration config) {
        this(config, new HttpClientBuilder(config));
    }

    public ApiClient(APIConfiguration config, HttpClientBuilder httpClientBuilder) {
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
                return new MexcResponse<>(body);
            } else {
                log.error("Request failed: {} ", response);
                throw new MexcApiException(response.code(), response.message());
            }
        } catch (IOException e) {
            log.error("Request failed: {} ", call.request(), e);
            throw new MexcApiException(e.getMessage(), e);
        }
    }
}
