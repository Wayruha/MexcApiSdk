package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerPrice {
    @JsonProperty("symbol")
    private String pairSymbol;
    private BigDecimal price;
}
