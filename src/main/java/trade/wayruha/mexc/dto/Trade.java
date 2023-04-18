package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import trade.wayruha.mexc.enums.OrderSide;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Trade {

    private String tradeId;

    @JsonAlias({"i"})
    private String orderId;

    @JsonAlias({"c"})
    private String clientOrderId;

    private Long orderListId;

    private OrderSide side;

    @JsonProperty("S")
    public void setOrderSideByAlias(int aliasNumber) {
        this.side = OrderSide.findByAlias(aliasNumber);
    }

    @JsonAlias({"p"})
    private BigDecimal price;

    @JsonProperty("origQty")
    @JsonAlias({"v", "qty"})
    private BigDecimal originalQuantity;

    @JsonProperty("quoteQty")
    @JsonAlias({"a"})
    private BigDecimal originalQuoteQuantity;

    @JsonAlias({"m"})
    private Boolean isMaker;

    private Boolean isBuyer;

    public Boolean getIsSeller() {
        if (isBuyer == null) {
            return null;
        }
        return !isBuyer;
    }

    private Boolean isBuyerMaker;
    private Boolean isBestMatch;

    @JsonAlias({"st"})
    private Boolean isSelfTrade;

    @JsonAlias({"n"})
    @JsonProperty("commission")
    private BigDecimal commissionAmount;

    @JsonAlias({"N"})
    @JsonProperty("commissionAsset")
    private String commissionAssetSymbol;

    @JsonProperty("time")
    @JsonAlias({"T", "createTime"})
    private Long createTimestamp;

    private Long updateTimestamp;

    //Public trade stream (spot@public.deals) push dealTime on 't' property
    //wherein Spot Account Deals (spot@private.deals) push tradeId on 't' property
    @JsonAlias({"t"})
    public void setTradeIdOrCreateTime(String data) {
        if (isNull(this.createTimestamp)) {
            try {
                this.createTimestamp = Long.parseLong(data);
            } catch (NumberFormatException e) {
                //continue
            }
        } else {
            this.tradeId = data;
        }
    }
}