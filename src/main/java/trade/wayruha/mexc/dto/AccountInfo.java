package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AccountInfo {
    private Integer makerCommission;
    private Integer takerCommission;
    private Integer buyerCommission;
    private Integer sellerCommission;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    @JsonProperty("updateTime")
    private Long updateTimestamp;
    //response timestamp
    private Long timestamp = Instant.now().toEpochMilli();
    private String accountType;
    private List<AccountAssetBalance> balances;
    private List<String> permissions;
}

