package trade.wayruha.mexc.security;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import trade.wayruha.mexc.Constants;
import trade.wayruha.mexc.security.SignatureUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;

/**
 * Copy-pasted from: https://github.com/mxcdevelop/mexc-api-demo/blob/main/Java/src/main/java/com/mexc/example/common/
 */
@Slf4j
public class SignatureInterceptor implements Interceptor {
    private static final String HEADER_ACCESS_KEY = "X-MEXC-APIKEY";
    private static final String TIMESTAMP_PARAM = "timestamp";
    private static final String SIGNATURE_PARAM = "signature";
    private final String apiKey;
    private final String secretKey;

    public SignatureInterceptor(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request origRequest = chain.request();
        String method = origRequest.method();
        //check
        final Request.Builder newBuilder = origRequest.newBuilder();
        final boolean isSigned = origRequest.header(Constants.ENDPOINT_SECURITY_SIGNED) != null;
        newBuilder.removeHeader(Constants.ENDPOINT_SECURITY_SIGNED);
        if (isSigned) {
            newBuilder.addHeader(HEADER_ACCESS_KEY, apiKey);
            final RequestBody origBody = origRequest.body();
            if (origBody != null && origBody.contentLength() > 0) {
                // encode RequestBody
                final String newBody = encodeBody(origBody);
                switch (method) {
                    case "POST":
                        newBuilder.post(RequestBody.create(MediaType.get("application/json"), newBody));
                        break;
                    case "DELETE":
                        newBuilder.delete(RequestBody.create(MediaType.get("application/json"), newBody));
                }
            } else {
                // encode Request URL
                final Request encodedRequest = encodeUrl(origRequest);
                return chain.proceed(encodedRequest);
            }
        }
        final Request request = newBuilder.build();
        return chain.proceed(request);
        /*Request newRequest;
        if ("GET".equals(method)) {
            newRequest = createUrlSignRequest(origRequest);
        } else if ("POST".equals(method) || "DELETE".equals(method)) {
            RequestBody origBody = origRequest.body();
            if (origBody != null) {
                //support body params
                newRequest = createBodySignRequest(origRequest, origBody, method);
            } else {
                //support query params
                newRequest = createUrlSignRequest(origRequest);
            }
        } else {
            return chain.proceed(origRequest);
        }
        return chain.proceed(newRequest);*/
    }

    private Request createBodySignRequest(Request origRequest, RequestBody origBody, String method) {
        String params = encodeBody(origBody);
        if ("POST".equals(method)) {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, apiKey)
                    .post(RequestBody.create(MediaType.get("text/plain"), params)).build();
        } else {
            return origRequest.newBuilder()
                    .addHeader(HEADER_ACCESS_KEY, apiKey)
                    .delete(RequestBody.create(MediaType.get("text/plain"), params)).build();
        }
    }

    private Request encodeUrl(Request request) {
        String timestamp = Instant.now().toEpochMilli() + "";
        HttpUrl url = request.url();
        HttpUrl.Builder urlBuilder = url
                .newBuilder()
                .setQueryParameter(TIMESTAMP_PARAM, timestamp);
        String queryParams = urlBuilder.build().query();
        final String signature = SignatureUtil.actualSignature(queryParams, secretKey);
        urlBuilder.setQueryParameter(SIGNATURE_PARAM, signature);
        return request.newBuilder()
                .addHeader(HEADER_ACCESS_KEY, apiKey)
                .url(urlBuilder.build()).build();
    }

    private String encodeBody(RequestBody origBody) {
        String timestamp = Instant.now().toEpochMilli() + "";
        String params = bodyToString(origBody);
        params += "&" + TIMESTAMP_PARAM + "=" + timestamp;
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

