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

    public String createListenKey() {
        final ListenKey data = client.executeSync(api.createListenKey()).getData();
        return ofNullable(data).map(ListenKey::getListenKey).orElse(null);
    }

    public String keepAliveListenKey(String listenKey) {
        final ListenKey data = client.executeSync(api.keepAliveListenKey(listenKey)).getData();
        return ofNullable(data).map(ListenKey::getListenKey).orElse(null);
    }

    public void deleteListenKey(String listenKey) {
        client.executeSync(api.deleteListenKey(listenKey));
    }
}
