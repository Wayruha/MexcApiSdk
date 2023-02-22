


For using websocket:
1. Create Callback implements WebSocketCallback that would process messages, like `trade.wayruha.mexc.ws.PublicWSConnectionTest.Callback`
2. Define config
   `MexcConfig conf = new MexcConfig("apiKey", "apiSecret");
   ApiClient apiClient = new ApiClient(conf);
   WSClientFactory factory = new WSClientFactory(apiClient);`
3. Define properties and socket itself
    `Set<String> symbols = Set.of("BTCUSDT", "ETHUSDT")
    WebSocketClient ws = factory.orderBookSubscription(symbols, TopLimit.TOP10, callback);`
4. Enjoy the flow
