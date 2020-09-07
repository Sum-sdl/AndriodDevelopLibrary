package com.sum.library_network.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by sdl on 2020/8/14
 * 多个Converter，只能有一个Converter处理数据
 */
public class StringConverterFactory extends Converter.Factory {

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        //请求体不处理
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //只处理String类型
        if (getRawType(type) == String.class) {
            return new StringResponseBodyConverter();
        } else {
            return super.responseBodyConverter(type, annotations, retrofit);
        }
    }

    private static class StringResponseBodyConverter implements Converter<ResponseBody, String> {

        @Override
        public String convert(ResponseBody value) throws IOException {
            try {
                return value.string();
            } finally {
                value.close();
            }
        }
    }

}
