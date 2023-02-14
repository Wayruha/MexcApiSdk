package trade.wayruha.mexc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MexcWSResponse<T> {
    @JsonProperty("d")
    private T data;
    @JsonProperty("c")
    private String channel;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("t")
    private long timestamp;
}
