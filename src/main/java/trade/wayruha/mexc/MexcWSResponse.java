package trade.wayruha.mexc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import static java.util.Objects.isNull;

@Data
public class MexcWSResponse<T> {

    //Properties for base response from stream
    private String id;
    private String code;
    @JsonProperty("msg")
    private String message;

    //Main properties
    @JsonProperty("d")
    private T data;
    @JsonProperty("c")
    private String channel;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("t")
    private long timestamp;

    public String toStringShort() {
        return "MexcWSResponse_Short{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", channel='" + channel + '\'' +
                ", symbol='" + symbol + '\'' +
                ", timestamp=" + timestamp +
                ", dataIsNull=" + isNull(data) +
                '}';
    }
}
