package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import trade.wayruha.mexc.enums.OrderSide;
import trade.wayruha.mexc.enums.OrderStatus;
import trade.wayruha.mexc.enums.OrderType;
import trade.wayruha.mexc.enums.TimeInForce;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String symbol;
    private long orderId;
    private long orderListId;
    private String clientOrderId;
    private BigDecimal price;
    private BigDecimal origQty;
    private BigDecimal executedQty;
    private BigDecimal cumulativeQuoteQty;
    private BigDecimal origQuoteOrderQty;
    private OrderStatus status;
    private TimeInForce timeInForce;
    private OrderType type;
    private OrderSide side;
    private BigDecimal stopPrice;
    private BigDecimal icebergQty;
    private long time;
    private long updateTime;
    private boolean isWorking;
}
