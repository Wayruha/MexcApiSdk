package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TickerPrice {
    @JsonProperty("symbol")
    private String pairSymbol;
    private BigDecimal price;
}
