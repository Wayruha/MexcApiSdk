package trade.wayruha.mexc.service;

import org.junit.Assert;
import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.Order;

import java.util.List;

import static trade.wayruha.mexc.util.TestConstants.API_KEY;
import static trade.wayruha.mexc.util.TestConstants.API_SECRET;

public class WalletServiceTest {
    MexcConfig apiConfiguration = new MexcConfig(API_KEY, API_SECRET);
    final WalletService walletService = new WalletService(apiConfiguration);

    @Test
    public void test_getAllOrders() {
        final String symbol = "BTCUSDT";
        final List<Order> allOrders = walletService.getAllOrders(symbol);
        Assert.assertTrue(allOrders.isEmpty());
    }
}
