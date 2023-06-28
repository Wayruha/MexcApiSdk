package trade.wayruha.mexc.service.endpoint;

import retrofit2.Call;
import retrofit2.http.*;
import trade.wayruha.mexc.constant.GlobalParams;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;

import java.util.List;

public interface TradeAPI {
    @POST("api/v3/order/test")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> testNewOrder(@Body PostOrder order);

    @POST("api/v3/order")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> newOrder(@Body PostOrder order);

    @DELETE("api/v3/order")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Order> cancelOrder(@Query("symbol") String symbol, @Query("orderId") String orderId,
                            @Query("origClientOrderId") String origClientOrderId, @Query("newClientOrderId") String newClientOrderId);

    /** maximum input 5 symbols,separated by ",". e.g. "BTCUSDT,MXUSDT,ADAUSDT" */
    @DELETE("api/v3/openOrders")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<List<Order>> cancelAllOrders(@Query("symbol") String symbolList);
}