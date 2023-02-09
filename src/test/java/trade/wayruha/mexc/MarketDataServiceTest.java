package trade.wayruha.mexc;

import org.junit.Test;
import trade.wayruha.mexc.client.APIConfiguration;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.service.MarketDataService;

import static org.junit.Assert.assertFalse;

public class MarketDataServiceTest {
    final MarketDataService service = new MarketDataService(new APIConfiguration());

    @Test
    public void test_loadExchangeInfo() {
        final ExchangeInfo exchangeInfo = service.getExchangeInfo();
        assertFalse(exchangeInfo.getSymbols().isEmpty());
    }
}
