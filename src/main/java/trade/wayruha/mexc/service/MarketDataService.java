package trade.wayruha.mexc.service;

import org.jetbrains.annotations.Nullable;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.dto.OrderBook;
import trade.wayruha.mexc.dto.TickerPrice;
import trade.wayruha.mexc.service.endpoint.MarketDataAPI;

import java.util.List;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MarketDataService extends ServiceBase {
    private final MarketDataAPI api;

    public MarketDataService(MexcConfig config) {
        super(config);
        this.api = createService(MarketDataAPI.class);
    }

    public ExchangeInfo getExchangeInfo() {
        return client.executeSync(api.getExchangeInfo()).getData();
    }

    /**
     * Get current order book for pair symbol
     *
     * @param limit default 100; max 5000
     */
    public OrderBook getOrderBookBySymbol(String pairSymbol, @Nullable Integer limit) {
        if (isNull(limit)) {
            limit = 100;
        }
        return client.executeSync(api.getOrderBookInfo(pairSymbol, limit)).getData();
    }

    /**
     * Get last ticker price for pairs
     *
     * @param pairSymbol If the symbol is null, tickers for all supported symbols will be returned in an array.
     */
    public List<TickerPrice> getTickerPriceInfo(@Nullable String pairSymbol) {
        if (isNotBlank(pairSymbol)) {
            return List.of(client.executeSync(api.getTickerPriceInfo(pairSymbol)).getData());
        }
        return client.executeSync(api.getTickerPriceInfo()).getData();
    }
}
