package trade.wayruha.mexc.service;

import org.junit.Assert;
import org.junit.Test;
import trade.wayruha.mexc.MexcApiException;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;
import trade.wayruha.mexc.enums.OrderSide;
import trade.wayruha.mexc.enums.OrderType;

import java.math.BigDecimal;
import java.util.UUID;

import static trade.wayruha.mexc.util.TestConstants.*;

public class TradeServiceTest {
    MexcConfig apiConfiguration = new MexcConfig(API_KEY, API_SECRET);
    final TradeService service = new TradeService(apiConfiguration);

    @Test
    public void test_newTestOrder() {
        final Order order = service.createNewTestOrder(makeOrder());
        Assert.assertNotNull(order);
    }

    private static PostOrder makeOrder() {
        return PostOrder.builder()
                .symbol(BTC_USD_PAIR_SYMBOL)
                .side(OrderSide.BUY).type(OrderType.MARKET)
                .quoteOrderQty(BigDecimal.TEN)
                .build();
    }

    @Test
    public void test_cancelOrder() {
        var testOrder = makeOrder();
        testOrder.setOrderId(UUID.randomUUID().toString());
        MexcApiException thrown = Assert.assertThrows(MexcApiException.class, () -> service.cancelOrder(testOrder));
    }

    @Test
    public void test_cancelAllOpenOrders() {
       var closedOrders = service.cancelAllOpenOrders(BTC_USD_PAIR_SYMBOL);
        Assert.assertNotNull(closedOrders);
    }
}
