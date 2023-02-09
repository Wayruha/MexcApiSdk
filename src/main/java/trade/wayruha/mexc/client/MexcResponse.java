package trade.wayruha.mexc.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class MexcResponse<T> {
    private int responseHttpStatus;
    @Getter
    private T data;
    private int cloudLimit;
    private String message;

    public MexcResponse(T data) {
        this.data = data;
    }

    public MexcResponse(int responseHttpStatus, String message) {
        this.responseHttpStatus = responseHttpStatus;
        this.message = message;
    }
}
