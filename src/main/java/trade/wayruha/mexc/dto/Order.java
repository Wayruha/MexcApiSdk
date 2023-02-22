package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import trade.wayruha.mexc.enums.OrderSide;
import trade.wayruha.mexc.enums.OrderStatus;
import trade.wayruha.mexc.enums.OrderType;
import trade.wayruha.mexc.enums.TimeInForce;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order{
    private String symbol;
    @JsonAlias({"i"})
    private String orderId;
    private long orderListId;
    @JsonAlias({"c"})
    private String clientOrderId;
    @JsonAlias({"p"})
    private BigDecimal price;
    @JsonAlias({"ap"})
    private BigDecimal avgPrice;
    @JsonAlias({"v"})
    private BigDecimal origQty;
    private BigDecimal executedQty;
    @JsonAlias({"A"})
    private BigDecimal remainAmount;
    @JsonAlias({"cv"})
    private BigDecimal cumulativeQuoteQty;
    @JsonAlias({"ca"})
    private BigDecimal cumulativeAmount;
    @JsonAlias({"a"})
    private BigDecimal origQuoteOrderQty;
    private OrderStatus status;
    private TimeInForce timeInForce;
    private OrderType type;
    private OrderSide side;
    private BigDecimal stopPrice;
    private BigDecimal icebergQty;

    @JsonAlias({"O", "createTime"})
    private long time;
    private long updateTime;
    private boolean isWorking;
    @JsonAlias({"m"})
    private boolean isMaker;

    @JsonProperty("o")
    public void setOrderTypeByAlias(int aliasNumber) {
        this.type = OrderType.findByAlias(aliasNumber);
    }

    @JsonProperty("s")
    public void setOrderStatusByAlias(int aliasNumber) {
        this.status = OrderStatus.findByAlias(aliasNumber);
    }

    @JsonProperty("S")
    public void setOrderSideByAlias(int aliasNumber) {
        this.side = OrderSide.findByAlias(aliasNumber);
    }

}
