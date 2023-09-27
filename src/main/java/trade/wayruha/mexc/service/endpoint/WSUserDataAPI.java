package trade.wayruha.mexc.service.endpoint;

import retrofit2.Call;
import retrofit2.http.*;
import trade.wayruha.mexc.constant.GlobalParams;
import trade.wayruha.mexc.dto.ListenKey;

public interface WSUserDataAPI {
    @POST("api/v3/userDataStream")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<ListenKey> createListenKey();

    @PUT("api/v3/userDataStream")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<ListenKey> keepAliveListenKey(@Query("listenKey") String listenKey);

    @DELETE("api/v3/userDataStream")
    @Headers(GlobalParams.ENDPOINT_SECURITY_SIGNED_HEADER)
    Call<Void> deleteListenKey(@Query("listenKey") String listenKey);
}
