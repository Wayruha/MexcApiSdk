package trade.wayruha.mexc.service.endpoint;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.dto.TickerPrice;

import java.util.List;

import static trade.wayruha.mexc.constant.ApiEndpoints.SYMBOL_PRICE_TICKER_API_PATH;

public interface MarketDataAPI {

    @GET("api/v3/exchangeInfo")
    Call<ExchangeInfo> getExchangeInfo();

    /** If the symbol is not sent, all symbols will be returned in an array.*/
    @GET(SYMBOL_PRICE_TICKER_API_PATH)
    Call<TickerPrice> getTickerPriceInfo(@NotNull @Query("symbol") String pairSymbol);

    @GET(SYMBOL_PRICE_TICKER_API_PATH)
    Call<List<TickerPrice>> getTickerPriceInfo();
}
