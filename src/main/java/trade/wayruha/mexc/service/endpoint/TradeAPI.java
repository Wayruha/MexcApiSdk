package trade.wayruha.mexc.service.endpoint;

import retrofit2.Call;
import retrofit2.http.*;
import trade.wayruha.mexc.constant.Constants;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;

import java.util.List;

public interface TradeAPI {
    @POST("api/v3/order/test")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> testNewOrder(@Body PostOrder order, @Query("recvWindow") Integer recvWindow, @Query("timestamp") Long timestamp);

    @POST("api/v3/order")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> newOrder(@Body PostOrder order, @Query("recvWindow") Integer recvWindow, @Query("timestamp") Long timestamp);

    @DELETE("api/v3/order")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> cancelOrder(@Query("symbol") String symbol, @Query("orderId") String orderId,
                            @Query("origClientOrderId") String origClientOrderId, @Query("newClientOrderId") String newClientOrderId,
                            @Query("recvWindow") Integer recvWindow, @Query("timestamp") Long timestamp);

    /** maximum input 5 symbols,separated by ",". e.g. "BTCUSDT,MXUSDT,ADAUSDT" */
    @DELETE("api/v3/openOrders")
    @Headers(Constants.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<List<Order>> cancelAllOrders(@Query("symbol") String symbolList, @Query("recvWindow") Integer recvWindow, @Query("timestamp") Long timestamp);
}