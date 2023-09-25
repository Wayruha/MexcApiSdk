package trade.wayruha.mexc.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Buy/Sell order side.
 */
@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public enum OrderSide {
    BUY(1),
    SELL(2);

    public OrderSide oppositeSide() {
        return this == BUY ? SELL : BUY;
    }

    private final int aliasNumber;

    public static OrderSide findByAlias(int aliasNumber) {
        return Arrays.stream(OrderSide.values()).parallel().filter(s -> s.getAliasNumber() == aliasNumber)
                .findFirst().orElse(null);
    }

    public static OrderSide findByName(String name) {
        return Arrays.stream(OrderSide.values()).parallel().filter(s -> s.name().equals(name))
                .findFirst().orElse(null);
    }
}
