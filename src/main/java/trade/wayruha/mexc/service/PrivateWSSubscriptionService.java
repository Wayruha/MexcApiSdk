package trade.wayruha.mexc.service;

import trade.wayruha.mexc.client.ApiClient;
import trade.wayruha.mexc.dto.ListenKey;
import trade.wayruha.mexc.service.endpoint.WSUserDataAPI;

import static java.util.Optional.ofNullable;

public class PrivateWSSubscriptionService extends ServiceBase {
    private final WSUserDataAPI api;

    public PrivateWSSubscriptionService(ApiClient client) {
        super(client);
        this.api = createService(WSUserDataAPI.class);
    }

    /**
     * Start a new user data stream. The stream will close after 60 minutes unless a keepalive is sent.
     * If the account has an active listenKey, that listenKey will be returned and its validity will be extended for 60 minutes.
     */
    public String createListenKey() {
        final ListenKey data = client.executeSync(api.createListenKey()).getData();
        return ofNullable(data).map(ListenKey::getListenKey).orElse(null);
    }

    /**
     * Keepalive a user data stream to prevent a time out. User data streams will close after 60 minutes.
     * It's recommended to send a ping about every 30 minutes.
     */
    public String keepAliveListenKey(String listenKey) {
        final ListenKey data = client.executeSync(api.keepAliveListenKey(listenKey)).getData();
        return ofNullable(data).map(ListenKey::getListenKey).orElse(null);
    }

    /**
     * Close out a user data stream.
     */
    public void deleteListenKey(String listenKey) {
        client.executeSync(api.deleteListenKey(listenKey));
    }
}
