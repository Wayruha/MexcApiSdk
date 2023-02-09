package trade.wayruha.mexc.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import trade.wayruha.mexc.Constants;
import trade.wayruha.mexc.dto.Order;

import java.util.List;

public interface WalletAPI {

    @GET("api/v3/allOrders")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<List<Order>> getOrders(@Query("symbol") String currency, @Query("limit") Integer limit, @Query("startTime") Long startTime,
                                @Query("endTime") Long endTime);

}
