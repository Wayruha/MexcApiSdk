package trade.wayruha.mexc.service;

import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;
import trade.wayruha.mexc.service.endpoint.TradeAPI;

public class TradeService extends ServiceBase {
    private final TradeAPI api;

    public TradeService(MexcConfig config) {
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
