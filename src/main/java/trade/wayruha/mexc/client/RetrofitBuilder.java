package trade.wayruha.mexc.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class RetrofitBuilder {

    public static Retrofit buildRetrofit(APIConfiguration configuration, OkHttpClient httpClient) {
        final ObjectMapper objectMapper = createObjectMapper();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(httpClient);
        builder.addConverterFactory(ObjectToMapConverterFactory.create(objectMapper));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        builder.baseUrl(configuration.getEndpoint());
        return builder.build();
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
