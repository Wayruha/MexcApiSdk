package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import trade.wayruha.mexc.enums.BalanceChangeType;

import java.math.BigDecimal;

@Data
public class AccountAssetBalance {

    @JsonProperty("asset")
    @JsonAlias({"a"})
    private String assetSymbol;

    @JsonProperty("free")
    @JsonAlias({"f"})
    private BigDecimal freeBalance;

    /**
     * free changed amount
     */
    @JsonProperty("cfree")
    @JsonAlias({"fd"})
    private BigDecimal freeBalanceDelta;


    @JsonProperty("locked")
    @JsonAlias({"l"})
    private BigDecimal frozenAmount;

    @JsonProperty("clocked")
    @JsonAlias({"ld"})
    private BigDecimal frozenAmountDelta;

    @JsonProperty("changetype")
    @JsonAlias({"o"})
    private BalanceChangeType changeType;

    /**
     * change time
     */
    @JsonProperty("ctime")
    @JsonAlias({"c"})
    private Long timestamp;
}
