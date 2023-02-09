package trade.wayruha.mexc.enums;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Type of order to submit to the system.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum OrderType {
  LIMIT,
  MARKET,
  LIMIT_MAKER,
  IMMEDIATE_OR_CANCEL,
  FILL_OR_KILL
}
