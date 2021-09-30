package com.bruce.common.advice;

import com.alibaba.fastjson.JSONObject;
import com.bruce.common.vo.RespData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * BaseResponseAdvice
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/22
 */
@RestControllerAdvice
public class BaseResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof RespData) {
            return body;
        } else if (selectedConverterType == StringHttpMessageConverter.class) {
            response.getHeaders().add("content-type", "application/json;charset=UTF-8");
            return JSONObject.toJSONString(RespData.success(body));
        } else {
            return RespData.success(body);
        }
    }
}
