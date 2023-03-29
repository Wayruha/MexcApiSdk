package trade.wayruha.mexc.service;

import org.junit.Assert;
import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.AccountInfo;
import trade.wayruha.mexc.dto.Order;

import java.util.List;

import static trade.wayruha.mexc.util.TestConstants.*;

public class WalletServiceTest {
    MexcConfig apiConfiguration = new MexcConfig(API_KEY, API_SECRET);
    final WalletService walletService = new WalletService(apiConfiguration);

    @Test
    public void test_getAllOrders() {
        final List<Order> allOrders = walletService.getAllOrders(BTC_USD_PAIR_SYMBOL);
        Assert.assertTrue(allOrders.isEmpty());
    }

    @Test
    public void test_getUserAccountInfo() {
        final AccountInfo userAccountInfo = walletService.getUserAccountInfo();
        Assert.assertNotNull(userAccountInfo);
    }
}
