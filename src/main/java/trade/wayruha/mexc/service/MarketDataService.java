package trade.wayruha.mexc.service;

import trade.wayruha.mexc.client.APIConfiguration;
import trade.wayruha.mexc.dto.ExchangeInfo;

public class MarketDataService extends ServiceBase {
    private final MarketDataAPI api;

    public MarketDataService(APIConfiguration config) {
        super(config);
        this.api = createService(MarketDataAPI.class);
    }

    public ExchangeInfo getExchangeInfo(){
        return client.executeSync(api.getExchangeInfo()).getData();
    }
}
