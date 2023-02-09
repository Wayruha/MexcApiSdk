package trade.wayruha.mexc.service;

import retrofit2.Call;
import retrofit2.http.*;
import trade.wayruha.mexc.Constants;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;

public interface TradeAPI {
//    @FormUrlEncoded
    @POST("api/v3/order/test")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> testNewOrder(@Body PostOrder order, @Query("recvWindow") Integer recvWindow, @Query("timestamp") Long timestamp);

    @POST("api/v3/order")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> newOrder(@Body PostOrder order, @Query("recvWindow") Integer recvWindow, @Query("timestamp") Long timestamp);
}
