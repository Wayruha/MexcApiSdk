package trade.wayruha.mexc.service;

import org.junit.Test;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.dto.OrderBook;
import trade.wayruha.mexc.dto.TickerPrice;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static trade.wayruha.mexc.util.TestConstants.BTC_USD_PAIR_SYMBOL;

public class MarketDataServiceTest {
    final MarketDataService service = new MarketDataService(new MexcConfig());

    @Test
    public void test_loadExchangeInfo() {
        final ExchangeInfo exchangeInfo = service.getExchangeInfo();
        assertFalse(exchangeInfo.getSymbols().isEmpty());
    }

    @Test
    public void test_loadOrderBook() {
        final OrderBook orderBook = service.getOrderBookBySymbol(BTC_USD_PAIR_SYMBOL, null);
        assertNotNull(orderBook);
        assertNotNull(orderBook.getAsks());
    }

    @Test
    public void test_loadTickerPriceInfoForSymbol() {
        final List<TickerPrice> tickerPriceList = service.getTickerPriceInfo(BTC_USD_PAIR_SYMBOL);
        assertFalse(tickerPriceList.isEmpty());
        assertNotNull(tickerPriceList.get(0).getPrice());
    }
}
