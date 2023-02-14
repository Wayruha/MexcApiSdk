package trade.wayruha.mexc.service;

import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.ExchangeInfo;

import static org.junit.Assert.assertFalse;

public class MarketDataServiceTest {
    final MarketDataService service = new MarketDataService(new MexcConfig());

    @Test
    public void test_loadExchangeInfo() {
        final ExchangeInfo exchangeInfo = service.getExchangeInfo();
        assertFalse(exchangeInfo.getSymbols().isEmpty());
    }
}
