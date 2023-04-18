package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import trade.wayruha.mexc.enums.*;

import java.math.BigDecimal;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private String symbol;

    @JsonAlias({"i"})
    private String orderId;

    private Long orderListId;

    @JsonAlias({"c"})
    private String clientOrderId;

    private OrderType type;
    @JsonProperty("o")
    public void setOrderTypeByAlias(int aliasNumber) {
        this.type = OrderType.findByAlias(aliasNumber);
    }

    private OrderSide side;
    @JsonProperty("S")
    public void setOrderSideByAlias(int aliasNumber) {
        this.side = OrderSide.findByAlias(aliasNumber);
    }

    private OrderStatus status;
    @JsonProperty("s")
    public void setOrderStatusByAlias(int aliasNumber) {
        this.status = OrderStatus.findByAlias(aliasNumber);
    }

    private boolean isWorking;

    @JsonAlias({"p"})
    private BigDecimal price;

    @JsonAlias({"P"})
    private BigDecimal stopPrice;


    private StopPriceType stopPriceType;

    @JsonAlias({"T"})
    public void setStopPriceTypeByAlias(int aliasNumber) {
        this.stopPriceType = StopPriceType.findByAlias(aliasNumber);
    }

    @JsonProperty("avgPrice")
    @JsonAlias({"ap"})
    private BigDecimal averagePrice;

    @JsonProperty("origQty")
    @JsonAlias({"v"})
    private BigDecimal originalQuantity;

    @JsonProperty("executedQty")
    @JsonAlias({"cv"})
    private BigDecimal executedQuantity;

    @JsonAlias({"V"})
    private BigDecimal remainQuantity;

    public BigDecimal getRemainQuantity(){
        if(isNull(remainQuantity) && nonNull(executedQuantity)){
            return originalQuantity.subtract(executedQuantity);
        }
        return remainQuantity;
    }

    @JsonProperty("icebergQty")
    private BigDecimal frozenQuantity;

    @JsonProperty("origQuoteOrderQty")
    @JsonAlias({"a"})
    private BigDecimal originalQuoteQuantity;

    @JsonProperty("cummulativeQuoteQty")
    @JsonAlias({"ca"})
    private BigDecimal executedQuoteQuantity;

    @JsonAlias({"A"})
    private BigDecimal remainQuoteQuantity;

    public BigDecimal getRemainQuoteQuantity(){
        if(isNull(remainQuoteQuantity) && nonNull(executedQuoteQuantity)){
             return originalQuoteQuantity.subtract(executedQuoteQuantity);
        }
        return remainQuoteQuantity;
    }

    @JsonAlias({"N"})
    @JsonProperty("commissionAsset")
    private String commissionAssetSymbol;

    @JsonAlias({"m"})
    private Boolean isMaker;

    private TimeInForce timeInForce;

    @JsonProperty("time")
    @JsonAlias({"O", "createTime"})
    private Long createTimestamp;

    @JsonProperty("updateTime")
    private Long updateTimestamp;
}
