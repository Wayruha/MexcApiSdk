package trade.wayruha.mexc.ws;

import okhttp3.Response;

@FunctionalInterface
public interface WebSocketCallback<T> {

    /**
     * Be called when the request successful.
     */
    void onResponse(T response);

    default void onClosed(int code, String reason){

    }

    default void onFailure(Throwable ex, Response response){

    }

    default void onOpen(Response response){

    }
}
