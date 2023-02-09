package trade.wayruha.mexc;

import lombok.Getter;

public class MexcApiException extends RuntimeException {
    @Getter
    private int code;

    public MexcApiException(String message) {
        super(message);
    }

    public MexcApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public MexcApiException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        if (this.code != 0) {
            return this.code + " : " + super.getMessage();
        }
        return super.getMessage();
    }
}
