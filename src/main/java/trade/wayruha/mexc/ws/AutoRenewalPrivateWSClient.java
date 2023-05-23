package trade.wayruha.mexc.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import trade.wayruha.mexc.MexcWSResponse;
import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.service.PrivateWSSubscriptionService;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static trade.wayruha.mexc.constant.GlobalParams.*;

/**
 * This WebSocket Client handles listenKey keep-alive as well as re-subscription (once in 23hrs) automatically
 */
@Slf4j
public class AutoRenewalPrivateWSClient<T> extends WebSocketClient<T> {
    private PrivateWSSubscriptionService wsService = new PrivateWSSubscriptionService(apiClient);
    private ScheduledExecutorService privateWsScheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledKeepAliveTask;
    private ScheduledFuture<?> scheduledResubscribeTask;

    AutoRenewalPrivateWSClient(Set<String> channels, WebSocketCallback<MexcWSResponse<T>> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        super(channels, listener, type, apiClient, mapper);
    }

    @Override
    protected void connect(Set<String> channels) {
        if (this.wsService == null) {
            this.wsService = new PrivateWSSubscriptionService(apiClient);
        }
        final String listenKey = wsService.createListenKey();
        log.trace("{} Got new private key: {} for WS channels: {}", logPrefix, listenKey, channels);
        this.connectionRequest = new Request.Builder()
                .url(config.getWebSocketHost() + "?" + LISTEN_KEY_QUERY_PARAM + "=" + listenKey)
                .build();
        super.connect(channels);

        this.scheduledKeepAliveTask = this.getPrivateScheduler().scheduleAtFixedRate(new KeepAlivePrivateWsTask(listenKey),
                WEB_SOCKET_KEEP_ALIVE_PERIOD_MIN, WEB_SOCKET_KEEP_ALIVE_PERIOD_MIN, TimeUnit.MINUTES);
        this.scheduledResubscribeTask = this.getPrivateScheduler().scheduleAtFixedRate(new ResubscribeTask(),
                WEB_SOCKET_PRIVATE_RESUBSCRIBE_PERIOD_MIN, WEB_SOCKET_PRIVATE_RESUBSCRIBE_PERIOD_MIN, TimeUnit.MINUTES);
    }

    private ScheduledExecutorService getPrivateScheduler() {
        if (isNull(this.privateWsScheduler)) {
            this.privateWsScheduler = Executors.newSingleThreadScheduledExecutor();
        }
        return this.privateWsScheduler;
    }

    @Override
    public void close() {
        super.close();
        scheduledKeepAliveTask.cancel(false);
        scheduledResubscribeTask.cancel(false);
    }

    class KeepAlivePrivateWsTask implements Runnable {
        private final String renewKey;

        public KeepAlivePrivateWsTask(String renewKey) {
            this.renewKey = renewKey;
        }

        @SneakyThrows
        @Override
        public void run() {
            try {
                log.trace("{} Try to keep alive private key {} for WS channels: {}", logPrefix, renewKey, channels);
                wsService.keepAliveListenKey(renewKey);
            } catch (Exception ex) {
                log.error("{} KeepAlive for private key {} ends with error for WS channels: {}. Reconnecting in {} sec...",
                        logPrefix, renewKey, channels, WEB_SOCKET_RECONNECTION_DELAY_MS / 1000);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
                reConnect();
            }
        }
    }

    class ResubscribeTask implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            log.trace("{} Try keep alive private WS channels: {}, by reconnect to channels", logPrefix, channels);
            reConnect();
        }
    }
}
