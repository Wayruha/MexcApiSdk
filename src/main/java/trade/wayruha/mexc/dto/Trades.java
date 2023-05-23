package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trades {

    @JsonProperty("deals")
    List<Trade> tradeList;

    private String symbol;

    private Long updateTimestamp;
}
