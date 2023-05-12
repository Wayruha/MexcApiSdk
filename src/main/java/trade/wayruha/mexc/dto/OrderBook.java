package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import trade.wayruha.mexc.utils.OrderBookLevelDeserializer;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBook {
    private String symbol;
    private List<Level> asks;
    private List<Level> bids;
    @JsonProperty("e")
    private String eventType;
    @JsonProperty("r")
    private long version;
    @JsonProperty("timestamp")
    private long updateTimestamp;
    private long lastUpdateId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonDeserialize(using = OrderBookLevelDeserializer.class)
    public static class Level {
        @JsonAlias({"p"})
        private BigDecimal price;

        @JsonAlias({"v"})
        private BigDecimal quantity;
    }
}
