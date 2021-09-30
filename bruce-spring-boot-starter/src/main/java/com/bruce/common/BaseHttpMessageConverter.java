package com.bruce.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.bruce.common.vo.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseHttpMessageConverter
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/29
 */
@Slf4j
public class BaseHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    private FastJsonConfig fastJsonConfig;

    public BaseHttpMessageConverter(FastJsonConfig fastJsonConfig) {
        if (fastJsonConfig == null) {
            log.error("fastJsonConfig is required when init HttpMessageConverter");
            throw new JSONException("fastJsonConfig is required when init HttpMessageConverter");
        }
        this.fastJsonConfig = fastJsonConfig;
    }

    @Override
    protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();
        byte[] bytes = JSON.toJSONBytes(obj, fastJsonConfig.getSerializeConfig(), fastJsonConfig.getSerializeFilters(), fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, fastJsonConfig.getSerializerFeatures());
        if (fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength(bytes.length);
        }
        OutputStream outputMessageBody = outputMessage.getBody();
        outputMessageBody.write(bytes);
        outputMessageBody.flush();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputMessageBody = inputMessage.getBody();
        return JSON.parseObject(inputMessageBody, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream inputMessageBody = inputMessage.getBody();
        byte[] bodyBytes = FileCopyUtils.copyToByteArray(inputMessageBody);
        try {
            return JSON.parseObject(new ByteArrayInputStream(bodyBytes), fastJsonConfig.getCharset(), type, fastJsonConfig.getFeatures());
        } catch (JSONException e) {
            throw new HttpMessageNotReadableException(e.getMessage(), inputMessage);
        }
    }

    @Override
    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        supportedMediaTypes.add(MediaType.ALL);
        super.setSupportedMediaTypes(supportedMediaTypes);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        super.setSupportedMediaTypes(new ArrayList<MediaType>() {{
            add(MediaType.ALL);
        }});
        return super.getSupportedMediaTypes();
    }

}
