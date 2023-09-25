package trade.wayruha.mexc.ws;

import lombok.SneakyThrows;
import okhttp3.Response;
import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.constant.GlobalParams;
import trade.wayruha.mexc.enums.OrderBookDepth;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;
import static trade.wayruha.mexc.constant.GlobalParams.DEFAULT_CONNECTION_TIMEOUT_MS;
import static trade.wayruha.mexc.constant.GlobalParams.WEB_SOCKET_CHANNEL_KEEP_ALIVE_PERIOD_SEC;
import static trade.wayruha.mexc.util.TestConstants.*;

public class PublicWSConnectionTest {
    final MexcConfig config = new MexcConfig();
    final WSClientFactory factory = new WSClientFactory(config);
    final AtomicInteger wsOpenCounter = new AtomicInteger();
    final AtomicInteger wsResponseCounter = new AtomicInteger();

    @Test
    public void test_OrderBookSubscription() {
        final Set<String> symbols = Set.of(BTC_USD_PAIR_SYMBOL, ETH_USD_PAIR_SYMBOL);
        final Callback callback = new Callback();
        final WebSocketClient<?> ws = factory.orderBookSubscription(symbols, OrderBookDepth.TOP5, callback);
        sleep(GlobalParams.WEB_SOCKET_RECONNECTION_DELAY_MS);
        assertTrue(wsOpenCounter.get() > 0);
        assertTrue(wsResponseCounter.get() > 0);
    }

    @Test
    public void test_ReconnectSubscriptions() {//check if ping was send to WS and our WS connect stable
        final Set<String> symbols = Set.of(UNUSED_PAIR_SYMBOL);
        final Callback callback = new Callback();
        final WebSocketClient<?> ws = factory.tradesSubscription(symbols, callback);
        sleep(DEFAULT_CONNECTION_TIMEOUT_MS);
        ws.reConnect();
        sleep(DEFAULT_CONNECTION_TIMEOUT_MS);
        assertTrue(wsOpenCounter.get() > 1);
        assertTrue(wsResponseCounter.get() > 2);
    }

    @Test
    public void test_NoTradesSubscriptionKeepUp() {//check if ping was send to WS and our WS connect stable
        final Set<String> symbols = Set.of(UNUSED_PAIR_SYMBOL);
        final Callback callback = new Callback();
        final WebSocketClient<?> ws = factory.tradesSubscription(symbols, callback);
        int keepAlivePeriodSec = (int)(WEB_SOCKET_CHANNEL_KEEP_ALIVE_PERIOD_SEC * 1.2); //+20%
        sleep(keepAlivePeriodSec  * 1000L);
        assertTrue(wsOpenCounter.get() > 0);
        assertTrue(wsResponseCounter.get() > 2);
    }

    @Test
    public void test_TradesSubscription() {
        final Set<String> symbols = Set.of(BTC_USD_PAIR_SYMBOL, ETH_USD_PAIR_SYMBOL);
        final Callback callback = new Callback();
        final WebSocketClient<?> ws = factory.tradesSubscription(symbols, callback);
        sleep(GlobalParams.WEB_SOCKET_RECONNECTION_DELAY_MS);
        assertTrue(wsOpenCounter.get() > 0);
        assertTrue(wsResponseCounter.get() > 0);
    }

    @Test
    public void test_DiffDepthSubscription() {
        final Set<String> symbols = Set.of(BTC_USD_PAIR_SYMBOL, ETH_USD_PAIR_SYMBOL);
        final Callback callback = new Callback();
        final WebSocketClient<?> ws = factory.diffDepthSubscription(symbols, callback);
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
