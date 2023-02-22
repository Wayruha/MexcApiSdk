package trade.wayruha.mexc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderBookDepth {
    TOP5(5),
    TOP10(10),
    TOP20(20),
    ;
    private final int depth;
}
