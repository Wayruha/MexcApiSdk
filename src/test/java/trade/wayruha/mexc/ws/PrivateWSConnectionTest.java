package trade.wayruha.mexc.ws;

import lombok.SneakyThrows;
import okhttp3.Response;
import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.constant.GlobalParams;
import trade.wayruha.mexc.service.PrivateWSSubscriptionService;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;
import static trade.wayruha.mexc.util.TestConstants.API_KEY;
import static trade.wayruha.mexc.util.TestConstants.API_SECRET;

public class PrivateWSConnectionTest {

    final MexcConfig config = new MexcConfig(API_KEY, API_SECRET);
    final ApiClient apiClient = new ApiClient(config);
    final WSClientFactory factory = new WSClientFactory(apiClient);
    final AtomicInteger wsOpenCounter = new AtomicInteger();
    final AtomicInteger wsResponseCounter = new AtomicInteger();
    final PrivateWSSubscriptionService privateAPI = new PrivateWSSubscriptionService(apiClient);

    @Test
    public void test_getUserSpotAccountOrderUpdates() {
        final Callback callback = new Callback();
        final WebSocketClient ws = factory.userSpotOrdersSubscription(callback);
        sleep(GlobalParams.WEB_SOCKET_RECONNECTION_DELAY_MS);
        assertTrue(wsOpenCounter.get() > 0);
        assertTrue(wsResponseCounter.get() > 0);
    }

    @Test
    public void test_getUserAccountAssetsUpdates() {
        final Callback callback = new Callback();
        final WebSocketClient ws = factory.userAccountAssetsSubscription(callback);
        sleep(GlobalParams.WEB_SOCKET_RECONNECTION_DELAY_MS);
        assertTrue(wsOpenCounter.get() > 0);
        assertTrue(wsResponseCounter.get() > 0);
    }
    @Test
    public void test_getUserSpotDealsUpdates() {
        final Callback callback = new Callback();
        final WebSocketClient ws = factory.userSpotDealsSubscription(callback);
        sleep(GlobalParams.WEB_SOCKET_RECONNECTION_DELAY_MS);
        assertTrue(wsOpenCounter.get() > 0);
        assertTrue(wsResponseCounter.get() > 0);
    }

    @SneakyThrows
    private static void sleep(long timeMs){
        Thread.sleep(timeMs);
    }

    class Callback implements WebSocketCallback{

        @Override
        public void onResponse(Object response) {
            System.out.println("Got response:" + response);
            wsResponseCounter.incrementAndGet();
        }

        @Override
        public void onClosed(int code, String reason) {
            WebSocketCallback.super.onClosed(code, reason);
        }

        @Override
        public void onFailure(Throwable ex, Response response) {
            WebSocketCallback.super.onFailure(ex, response);
        }

        @Override
        public void onOpen(Response response) {
            WebSocketCallback.super.onOpen(response);
            wsOpenCounter.incrementAndGet();
        }
    }
}
