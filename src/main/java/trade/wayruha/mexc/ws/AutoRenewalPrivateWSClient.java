package trade.wayruha.mexc.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.service.PrivateWSSubscriptionService;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static trade.wayruha.mexc.Constants.LISTEN_KEY_QUERY_PARAM;
import static trade.wayruha.mexc.constant.GlobalParams.*;

/**
 * This WebSocket Client handles listenKey keep-alive as well as re-subscription (once in 23hrs) automatically
 * !!!WARNING!!! Not tested!
 */
@Slf4j
public class AutoRenewalPrivateWSClient<T> extends WebSocketClient<T> {
    private PrivateWSSubscriptionService wsService = new PrivateWSSubscriptionService(apiClient);
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> scheduledKeepAliveTask;
    private ScheduledFuture<?> scheduledResubscribeTask;

    AutoRenewalPrivateWSClient(Set<String> channels, WebSocketCallback<T> listener, Class<T> type, ApiClient apiClient, ObjectMapper mapper) {
        super(channels, listener, type, apiClient, mapper);
    }

    @Override
    protected void connect(Set<String> channels) {
        if (this.wsService == null) {
            this.wsService = new PrivateWSSubscriptionService(apiClient);
        }
        final String listenKey = wsService.createListenKey();
        this.connectionRequest = new Request.Builder()
                .url(config.getWebSocketHost() + "?" + LISTEN_KEY_QUERY_PARAM + "=" + listenKey)
                .build();
        super.connect(channels);

        final KeepAliveTask keepAliveTask = new KeepAliveTask(listenKey);
        this.scheduledKeepAliveTask = scheduler.scheduleAtFixedRate(keepAliveTask, WEB_SOCKET_KEEP_ALIVE_PERIOD_MIN, WEB_SOCKET_KEEP_ALIVE_PERIOD_MIN, TimeUnit.MINUTES);
        this.scheduledResubscribeTask = scheduler.scheduleAtFixedRate(new ResubscribeTask(), WEB_SOCKET_RESUBSCRIBE_PERIOD_MIN, WEB_SOCKET_RESUBSCRIBE_PERIOD_MIN, TimeUnit.MINUTES);
    }

    @Override
    public void close() {
        super.close();
        scheduledKeepAliveTask.cancel(false);
        scheduledResubscribeTask.cancel(false);
    }

    class KeepAliveTask implements Runnable {
        private final String renewKey;

        public KeepAliveTask(String renewKey) {
            this.renewKey = renewKey;
        }

        @SneakyThrows
        @Override
        public void run() {
            try {
                wsService.keepAliveListenKey(renewKey);
            } catch (Exception ex) {
                log.error("{} KeepAlive {} error. Reconnecting in {} sec...", logPrefix, renewKey, WEB_SOCKET_RECONNECTION_DELAY_MS / 1000);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
                reConnect();
            }
        }
    }

    class ResubscribeTask implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            try {
                reConnect();
            } catch (Exception ex) {
                log.error("{} Re-subscription error. Try again in {} sec...", logPrefix, WEB_SOCKET_RECONNECTION_DELAY_MS / 1000);
                Thread.sleep(WEB_SOCKET_RECONNECTION_DELAY_MS);
                reConnect();
            }
        }
    }
}
