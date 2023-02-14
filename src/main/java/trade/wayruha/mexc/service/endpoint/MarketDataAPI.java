package trade.wayruha.mexc.service.endpoint;

import retrofit2.Call;
import retrofit2.http.GET;
import trade.wayruha.mexc.dto.ExchangeInfo;

public interface MarketDataAPI {

    @GET("api/v3/exchangeInfo")
    Call<ExchangeInfo> getExchangeInfo();
}
