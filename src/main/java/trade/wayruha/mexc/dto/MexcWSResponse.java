package trade.wayruha.mexc.dto;

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
    private Long timestamp;

    @Override
    public String toString() {
        return "MexcWSResponse{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", channel='" + channel + '\'' +
                ", symbol='" + symbol + '\'' +
                ", timestamp=" + timestamp +
                ", dataIsNull=" + isNull(data) +
                '}';
    }
}
