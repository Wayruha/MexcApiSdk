package trade.wayruha.mexc.service.endpoint;

import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.dto.OrderBook;
import trade.wayruha.mexc.dto.TickerPrice;

import java.util.List;

public interface MarketDataAPI {

    @GET("api/v3/exchangeInfo")
    Call<ExchangeInfo> getExchangeInfo();

    @GET("/api/v3/depth")
    Call<OrderBook> getOrderBookInfo(@NotNull @Query("symbol") String pairSymbol,
                                     @NotNull @Query("limit") Integer limit);

    /** If the symbol is not sent, all symbols will be returned in an array.*/
    @GET("/api/v3/ticker/price")
    Call<TickerPrice> getTickerPriceInfo(@NotNull @Query("symbol") String pairSymbol);

    @GET("/api/v3/ticker/price")
    Call<List<TickerPrice>> getTickerPriceInfo();
}
