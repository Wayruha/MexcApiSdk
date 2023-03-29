package trade.wayruha.mexc.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MexcResponse<T> {
    private int responseHttpStatus;
    private T data;
    private int cloudLimit;
    private String message;

    public MexcResponse(int responseHttpStatus, T data) {
        this.data = data;
        this.responseHttpStatus = responseHttpStatus;
    }

    public MexcResponse(int responseHttpStatus, String message) {
        this.responseHttpStatus = responseHttpStatus;
        this.message = message;
    }
}
