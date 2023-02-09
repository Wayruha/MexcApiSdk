package trade.wayruha.mexc;

import org.junit.Assert;
import org.junit.Test;
import trade.wayruha.mexc.client.APIConfiguration;
import trade.wayruha.mexc.dto.Order;
import trade.wayruha.mexc.service.WalletService;

import java.util.List;

public class WalletServiceTest {
    APIConfiguration apiConfiguration = new APIConfiguration("", "");
    final WalletService walletService = new WalletService(apiConfiguration);

    @Test
    public void test_getAllOrders() {
        final String symbol = "BTCUSDT";
        final List<Order> allOrders = walletService.getAllOrders(symbol);
        Assert.assertTrue(allOrders.isEmpty());
    }
}
