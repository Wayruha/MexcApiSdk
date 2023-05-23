package trade.wayruha.mexc.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import trade.wayruha.mexc.dto.OrderBook;

import java.io.IOException;
import java.math.BigDecimal;

public class OrderBookLevelDeserializer extends JsonDeserializer<OrderBook.Level> {

    @Override
    public OrderBook.Level deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        BigDecimal price;
        BigDecimal quantity;
        if (node.isArray()) {
            price = new BigDecimal(node.get(0).asText());
            quantity = new BigDecimal(node.get(1).asText());
        } else {
            price = new BigDecimal((node.get("p").asText()));
            quantity = new BigDecimal((node.get("v").asText()));
        }
        return new OrderBook.Level(price, quantity);
    }
}