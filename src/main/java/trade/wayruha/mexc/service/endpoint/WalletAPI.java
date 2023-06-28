package trade.wayruha.mexc.service.endpoint;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import trade.wayruha.mexc.constant.GlobalParams;
import trade.wayruha.mexc.dto.AccountInfo;
import trade.wayruha.mexc.dto.Order;

import java.util.List;


public interface WalletAPI {

    @GET("api/v3/allOrders")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<List<Order>> getOrders(@Query("symbol") String symbol, @Query("limit") Integer limit, @Query("startTime") Long startTime,
                                @Query("endTime") Long endTime);

    @GET("api/v3/order")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> getOrder(@Query("symbol") String symbol, @Query("orderId") String orderId,
                         @Query("origClientOrderId") String origClientOrderId);

    @GET("/api/v3/account")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<AccountInfo> getAccountInfo();
}
