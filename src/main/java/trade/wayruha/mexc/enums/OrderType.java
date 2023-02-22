package trade.wayruha.mexc.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Type of order to submit to the system.
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public enum OrderType {
  LIMIT(1), //aka LIMIT_ORDER
  MARKET(5), //aka MARKET_ORDER
  LIMIT_MAKER(2), // aka POST_ONLY
  IMMEDIATE_OR_CANCEL(3),
  FILL_OR_KILL(4),
  STOP_LIMIT(100);
  private final int aliasNumber;

  public static OrderType findByAlias(int aliasNumber){
    return Arrays.stream(OrderType.values()).parallel().filter(s-> s.getAliasNumber() == aliasNumber)
            .findFirst().orElse(null);
  }
}
