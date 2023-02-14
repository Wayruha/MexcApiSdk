package trade.wayruha.mexc.ws;

public enum WSState {
  IDLE,
  DELAY_CONNECT,
  CONNECTED,
  CLOSED_ON_ERROR,
  CONNECTING
}
