package trade.wayruha.mexc.service;

import trade.wayruha.mexc.client.APIConfiguration;
import trade.wayruha.mexc.dto.Order;

import java.util.List;

public class WalletService extends ServiceBase {
    private WalletAPI api;

    public WalletService(APIConfiguration config) {
        super(config);
        this.api = client.createService(WalletAPI.class);
    }

    public List<Order> getAllOrders(String symbol) {
        return getAllOrders(symbol, null, null, null);
    }

    public List<Order> getAllOrders(String symbol, Integer limit, Long startTime, Long endTime) {
        return client.executeSync(api.getOrders(symbol, limit, startTime, endTime)).getData();

    }
}
