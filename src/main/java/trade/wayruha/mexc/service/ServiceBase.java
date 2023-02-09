package trade.wayruha.mexc.service;

import trade.wayruha.mexc.Constants;
import trade.wayruha.mexc.client.APIConfiguration;
import trade.wayruha.mexc.client.ApiClient;

public abstract class ServiceBase {
    protected final ApiClient client;
    protected int receivingWindow = Constants.DEFAULT_RECEIVING_WINDOW;

    public ServiceBase(ApiClient client) {
        this.client = client;
    }

    public ServiceBase(APIConfiguration config) {
        this.client = new ApiClient(config);
    }

    protected <T> T createService(Class<T> apiClass) {
        return client.createService(apiClass);
    }

    protected long now() {
        return System.currentTimeMillis();
    }

}
