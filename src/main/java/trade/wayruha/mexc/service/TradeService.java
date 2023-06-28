package trade.wayruha.mexc.service;

import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;
import trade.wayruha.mexc.enums.OrderStatus;
import trade.wayruha.mexc.service.endpoint.TradeAPI;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

public class TradeService extends ServiceBase {
    private final TradeAPI api;

    public TradeService(MexcConfig config) {
        super(config);
        this.api = createService(TradeAPI.class);
    }

    public Order createNewTestOrder(PostOrder newOrder) {
        var createdOrder = client.executeSync(api.testNewOrder(newOrder)).getData();
        //mexc actually doesn't create test order till 01/05/2023. So we check to emulate answer
        if (isNull(createdOrder) || isNull(createdOrder.getSymbol())) {
            createdOrder = Order.builder()
                    .symbol(newOrder.getSymbol())
                    .side(newOrder.getSide())
                    .type(newOrder.getType())
                    .originalQuantity(newOrder.getQuantity())
                    .originalQuoteQuantity(newOrder.getQuoteOrderQty())
                    .price(newOrder.getPrice())
                    .orderId(UUID.randomUUID().toString())
                    .status(OrderStatus.NEW)
                    .createTimestamp(System.currentTimeMillis())
                    .build();
        }
        return createdOrder;
    }

    public Order createNewOrder(PostOrder order) {
        return client.executeSync(api.newOrder(order)).getData();
    }

    public Order cancelOrder(PostOrder order) {
        return client.executeSync(api.cancelOrder(order.getSymbol(), order.getOrderId(), order.getClientOrderId(),
                order.getNewClientOrderId())).getData();
    }

    public List<Order> cancelAllOpenOrders(String tradingSymbol) {
        return client.executeSync(api.cancelAllOrders(tradingSymbol)).getData();
    }
}
