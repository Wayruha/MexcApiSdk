package trade.wayruha.mexc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum OrderBookDepth {
    TOP5(5),
    TOP10(10),
    TOP20(20),
    ;
    private final int depth;

    public static Optional<OrderBookDepth> findByDepth(int depth) {
        return Arrays.stream(OrderBookDepth.values()).parallel().filter(v -> v.depth == depth).findFirst();
    }
}
