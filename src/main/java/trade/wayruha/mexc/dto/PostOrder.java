package trade.wayruha.mexc.dto;

import lombok.Builder;
import lombok.Data;
import trade.wayruha.mexc.enums.OrderSide;
import trade.wayruha.mexc.enums.OrderType;

import java.math.BigDecimal;

@Data
@Builder
public class PostOrder {
    private String symbol;
    private OrderSide side;
    private OrderType type;
    private BigDecimal quantity;
    private BigDecimal quoteOrderQty;
    private BigDecimal price;
    private String newClientOrderId;

    /**
     * validate if all necessary qty and price params are passed based on OrderType
     */
    public boolean validate(){
        if(type == OrderType.LIMIT) return quantity != null && price != null;
        if(type == OrderType.MARKET) return quantity != null || quoteOrderQty != null;
        return false;
    }
}
