package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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

    @Data
    public static class Level {
        @JsonProperty("p")
        private BigDecimal price;
        @JsonProperty("v")
        private BigDecimal quantity;
    }
}
