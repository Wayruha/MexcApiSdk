package trade.wayruha.mexc.service;

import org.junit.Assert;
import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.dto.PostOrder;
import trade.wayruha.mexc.enums.OrderSide;
import trade.wayruha.mexc.enums.OrderType;

import java.math.BigDecimal;

public class TradeServiceTest {
    MexcConfig apiConfiguration = new MexcConfig("", "");
    final TradeService service = new TradeService(apiConfiguration);

    @Test
    public void test_newTestOrder() {
        final Order order = service.testNewOrder(makeOrder());
        Assert.assertNotNull(order);
    }

    private static PostOrder makeOrder() {
        return PostOrder.builder()
                .symbol("BTCUSDT")
                .side(OrderSide.BUY).type(OrderType.MARKET)
                .quoteOrderQty(BigDecimal.TEN)
                .build();
    }
}
