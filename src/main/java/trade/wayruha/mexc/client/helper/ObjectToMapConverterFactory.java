package trade.wayruha.mexc.client.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ObjectToMapConverterFactory extends Converter.Factory {
    private final ObjectMapper mapper;
    private final JacksonConverterFactory responseConverterFactory;

    private ObjectToMapConverterFactory(ObjectMapper paramsMapper) {
        this.mapper = paramsMapper;
        this.responseConverterFactory = JacksonConverterFactory.create(mapper);
    }

    public static ObjectToMapConverterFactory create() {
        return create(new ObjectMapper());
    }

    public static ObjectToMapConverterFactory create(ObjectMapper paramsMapper) {
        if (paramsMapper == null) {
            throw new NullPointerException("paramsMapper == null");
        } else {
            return new ObjectToMapConverterFactory(paramsMapper);
        }
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return responseConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new ObjectToMapConverter();
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return super.stringConverter(type, annotations, retrofit);
    }

    static final class ObjectToMapConverter implements Converter<Object, RequestBody> {
        private ObjectMapper mapper = new ObjectMapper();

        @Override
        public RequestBody convert(Object value) {
            final String content = convertToUrlEncoded(value);
            return RequestBody.create(MediaType.get("text/plain"), content);
        }

        public String convertToUrlEncoded(Object obj) {

            Map<String, Object> map = mapper.convertValue(obj, Map.class);
            final String content = map.entrySet().stream()
                    .filter(e -> nonNull(e.getValue()))
                    .map(e -> {
                        String val = e.getValue().toString();
                        if (e.getValue() instanceof BigDecimal) {
                            val = ((BigDecimal) e.getValue()).toPlainString();
                        }
                        return e.getKey() + "=" + val;
                    })
                    .collect(Collectors.joining("&"));
            return content;


          /*  final JsonNode tree = mapper.convertValue(obj, JsonNode.class);
            final Iterator<String> iter = tree.fieldNames();
            final String content = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), false)
                    .map(name -> Pair.of(name, tree.get(name)))
                    .filter(pair -> pair.getRight().isNull())
                    .map(pair -> {
                        final String name = pair.getLeft();
                        final JsonNode valNode = pair.getRight();
                        if(valNode.isNumber()) {
                            return Pair.of(name, String.valueOf(valNode.numberValue()));
                        }
                        return Pair.of(name, valNode.asText());
                    })
                    .map(pair -> pair.getLeft() + "=" + pair.getRight())
                    .collect(Collectors.joining("&"));
            return content;
*/
            /*Map<String, String> map = mapper.convertValue(obj, Map.class);

            return map.keySet().stream()
                    .filter(key -> map.get(key) != null)
                    .map(key -> key+"="+map.get(key))
                    .collect(Collectors.joining("&"));*/
                    /*.map(key -> {
                        try {
                            Class typeClass = obj.getClass().getDeclaredField(key).getType();
                            String type = typeClass.getSimpleName();
                            String value = null;

                            if (type.equals("String")) {
                                value = map.get(key);
                            } else if (typeClass.isPrimitive()) {
                                value = String.valueOf(map.get(key));
                            }
                            return value != null && value.length() > 0
                                    ? key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
                                    : null;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            throw new UnsupportedOperationException(); // ???
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("&"));*/
        }
    }
}
