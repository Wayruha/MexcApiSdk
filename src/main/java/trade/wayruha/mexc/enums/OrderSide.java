package trade.wayruha.mexc.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Buy/Sell order side.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum OrderSide {
    BUY,
    SELL;

    public OrderSide oppositeSide() {
        return this == BUY ? SELL : BUY;
    }
}
