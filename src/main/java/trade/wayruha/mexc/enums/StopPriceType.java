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
public enum StopPriceType {
    GE(0),//price is higher than triggerPrice
    LE(1);//price is lower than triggerPrice

    private final int aliasNumber;

    public static StopPriceType findByAlias(int aliasNumber){
        return Arrays.stream(StopPriceType.values()).parallel().filter(s-> s.getAliasNumber() == aliasNumber)
                .findFirst().orElse(null);
    }

    public static StopPriceType findByName(String name){
        return Arrays.stream(StopPriceType.values()).parallel().filter(s-> s.name().equals(name))
                .findFirst().orElse(null);
    }
}
