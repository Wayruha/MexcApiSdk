package trade.wayruha.mexc.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import java.util.List;

@Data
public class ExchangeInfo {
    @JsonUnwrapped
    List<SymbolInfo> symbols;
}
