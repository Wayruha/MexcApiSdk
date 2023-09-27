package trade.wayruha.mexc.service;

import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.AccountInfo;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.service.endpoint.WalletAPI;

import java.util.List;

public class WalletService extends ServiceBase {
    private final WalletAPI api;

    public WalletService(MexcConfig config) {
        super(config);
        this.api = client.createService(WalletAPI.class);
    }

    public List<Order> getAllOrders(String symbol) {
        return this.getAllOrders(symbol, null, null, null);
    }

    public List<Order> getAllOrders(String symbol, Integer limit, Long startTime, Long endTime) {
        return client.executeSync(api.getOrders(symbol, limit, startTime, endTime)).getData();
    }

    public Order getOrder(String symbol, String orderId) {
        return this.getOrder(symbol, orderId, null);
    }

    public Order getOrder(String symbol, String orderId, String origClientOrderId) {
        return client.executeSync(api.getOrder(symbol, orderId, origClientOrderId)).getData();
    }

    public AccountInfo getUserAccountInfo() {
        return client.executeSync(api.getAccountInfo()).getData();
    }
}
