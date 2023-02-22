package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import trade.wayruha.mexc.enums.OrderType;
import trade.wayruha.mexc.enums.Permission;
import trade.wayruha.mexc.enums.TradeSymbolStatus;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SymbolInfo{
    private String symbol;
    private TradeSymbolStatus status;
    private String baseAsset;
    private String quoteAsset;
    private int baseAssetPrecision;
    private int quoteAssetPrecision;
    private int baseCommissionPrecision;
    private int quoteCommissionPrecision;
    private List<OrderType> orderTypes;
    private boolean quoteOrderQtyMarketAllowed;
    private boolean isSpotTradingAllowed;
    private boolean isMarginTradingAllowed;
    private int quoteAmountPrecision;
    private BigDecimal baseSizePrecision;
    private List<Permission> permissions;
    private List<Filter> filters;
    private BigDecimal maxQuoteAmount;
    private BigDecimal makerCommission;
    private BigDecimal takerCommission;

    public static class Filter {

    }

    @JsonSetter
    public void setQuoteAmountPrecision(String quoteAmountPrecision) {
        this.quoteAmountPrecision = (int) Double.parseDouble(quoteAmountPrecision);
    }
}
