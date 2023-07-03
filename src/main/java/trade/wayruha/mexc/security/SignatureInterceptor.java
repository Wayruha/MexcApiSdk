package trade.wayruha.mexc.security;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import trade.wayruha.mexc.constant.GlobalParams;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;

/**
 * Copy-pasted from: <a href="https://github.com/mxcdevelop/mexc-api-demo/blob/main/Java/src/main/java/com/mexc/example/common/">Mexc official examples</a>
 */
@Slf4j
public class SignatureInterceptor implements Interceptor {
    private static final String HEADER_ACCESS_KEY = "X-MEXC-APIKEY";
    private static final String TIMESTAMP_PARAM = "timestamp";
    private static final String REQUEST_WINDOW_PARAM = "recvWindow";
    private static final String SIGNATURE_PARAM = "signature";
    private final String apiKey;
    private final String secretKey;
    private final long recvWindow;

    public SignatureInterceptor(String apiKey, String secretKey, long recvWindow) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.recvWindow = recvWindow;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request origRequest = chain.request();
        String method = origRequest.method();
        //check
        final Request.Builder newBuilder = origRequest.newBuilder();
        final boolean isSigned = origRequest.header(GlobalParams.ENDPOINT_SECURITY_SIGNED) != null;
        newBuilder.removeHeader(GlobalParams.ENDPOINT_SECURITY_SIGNED);
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        if (isSigned) {
            newBuilder.addHeader(HEADER_ACCESS_KEY, apiKey);
            final RequestBody origBody = origRequest.body();
            if (origBody != null && origBody.contentLength() > 0) {
                // encode RequestBody
                final String newBody = encodeBody(origBody, timestamp);
                switch (method) {
                    case "POST":
                        newBuilder.post(RequestBody.create(MediaType.get("application/json"), newBody));
                        break;
                    case "DELETE":
                        newBuilder.delete(RequestBody.create(MediaType.get("application/json"), newBody));
                }
            } else {
                // encode Request URL
                final Request encodedRequest = encodeUrl(origRequest, timestamp);
                return chain.proceed(encodedRequest);
            }
        }
        final Request request = newBuilder.build();
        return chain.proceed(request);
    }

    private Request encodeUrl(Request request, String timestamp) {
        HttpUrl url = request.url();
        HttpUrl.Builder urlBuilder = url
                .newBuilder()
                .setQueryParameter(TIMESTAMP_PARAM, timestamp)
                .setQueryParameter(REQUEST_WINDOW_PARAM, String.valueOf(recvWindow));
        String queryParams = urlBuilder.build().query();
        final String signature = SignatureUtil.actualSignature(queryParams, secretKey);
        urlBuilder.setQueryParameter(SIGNATURE_PARAM, signature);
        return request.newBuilder()
                .addHeader(HEADER_ACCESS_KEY, apiKey)
                .url(urlBuilder.build()).build();
    }

    private String encodeBody(RequestBody origBody, String timestamp) {
        String params = bodyToString(origBody);
        params += "&" + TIMESTAMP_PARAM + "=" + timestamp;
        params += "&" + REQUEST_WINDOW_PARAM + "=" + recvWindow;
        String signature = SignatureUtil.actualSignature(params, secretKey);
        params += "&" + SIGNATURE_PARAM + "=" + signature;
        return params;
    }

    private String bodyToString(RequestBody body) {
        try {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            return buffer.readString(Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

