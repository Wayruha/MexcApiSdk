package trade.wayruha.mexc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceChangeType {
    WITHDRAW("withdraw"),
    WITHDRAW_FEE("withdraw fee"),
    DEPOSIT("deposit"),
    DEPOSIT_FEE(" deposit fee"),
    ENTRUST("deal"),
    ENTRUST_PLACE("place order"),
    ENTRUST_CANCEL("cancel order"),
    TRADE_FEE("trade fee"),
    ENTRUST_UNFROZEN("return frozen order funds"),
    SUGAR("airdrop"),
    ETF_INDEX("ETF place order");

    private final String description;
}
