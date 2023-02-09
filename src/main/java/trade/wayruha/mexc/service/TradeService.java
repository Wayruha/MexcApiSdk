package trade.wayruha.mexc.service;

import trade.wayruha.mexc.client.APIConfiguration;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;

public class TradeService extends ServiceBase {
    private final TradeAPI api;

    public TradeService(APIConfiguration config) {
        super(config);
        this.api = createService(TradeAPI.class);
    }

    public Order testNewOrder(PostOrder order) {
        return client.executeSync(api.testNewOrder(order, null, null)).getData();
    }

    public Order newOrder(PostOrder order) {
        return client.executeSync(api.newOrder(order, null, null)).getData();
    }
}
