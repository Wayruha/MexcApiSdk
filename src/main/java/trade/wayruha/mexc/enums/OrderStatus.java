package trade.wayruha.mexc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    CREATED(0),
    NEW(1),
    FILLED(2),
    PARTIALLY_FILLED(3),
    CANCELED(4),
    PARTIALLY_CANCELED(5),
    ERROR(999);
    private final int aliasNumber;

    public static OrderStatus findByAlias(int aliasNumber) {
        return Arrays.stream(OrderStatus.values()).parallel().filter(s -> s.getAliasNumber() == aliasNumber)
                .findFirst().orElse(null);
    }

    public boolean isFinished() {
        return this != NEW && this != PARTIALLY_FILLED;
    }

    public boolean isExecuted() {
        return this == FILLED || this == PARTIALLY_FILLED;
    }
}
