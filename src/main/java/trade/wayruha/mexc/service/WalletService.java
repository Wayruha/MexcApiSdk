package trade.wayruha.mexc.service;

import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.service.endpoint.WalletAPI;

import java.util.List;

public class WalletService extends ServiceBase {
    private WalletAPI api;

    public WalletService(MexcConfig config) {
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
