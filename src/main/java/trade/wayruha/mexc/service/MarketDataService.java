package trade.wayruha.mexc.service;

import org.jetbrains.annotations.Nullable;
import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.dto.TickerPrice;
import trade.wayruha.mexc.service.endpoint.MarketDataAPI;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MarketDataService extends ServiceBase {
    private final MarketDataAPI api;

    public MarketDataService(MexcConfig config) {
        super(config);
        this.api = createService(MarketDataAPI.class);
    }

    public ExchangeInfo getExchangeInfo(){
        return client.executeSync(api.getExchangeInfo()).getData();
    }

    /** If the symbol is not sent, all symbols will be returned in an array.*/
    public List<TickerPrice> getTickerPriceInfo(@Nullable String pairSymbol){
        if(isNotBlank(pairSymbol)){
            return List.of(client.executeSync(api.getTickerPriceInfo(pairSymbol)).getData());
        }
        return client.executeSync(api.getTickerPriceInfo()).getData();
    }
}
