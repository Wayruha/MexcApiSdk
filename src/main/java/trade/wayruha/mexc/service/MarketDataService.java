package trade.wayruha.mexc.service;

import trade.wayruha.mexc.MexcConfig;
import trade.wayruha.mexc.dto.ExchangeInfo;
import trade.wayruha.mexc.service.endpoint.MarketDataAPI;

public class MarketDataService extends ServiceBase {
    private final MarketDataAPI api;

    public MarketDataService(MexcConfig config) {
        super(config);
        this.api = createService(MarketDataAPI.class);
    }

    public ExchangeInfo getExchangeInfo(){
        return client.executeSync(api.getExchangeInfo()).getData();
    }
}
