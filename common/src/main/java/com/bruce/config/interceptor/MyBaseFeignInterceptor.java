package com.bruce.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.bruce.common.interceptor.BaseFeignInterceptor;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseFeignInterceptor
 * 类描述：feign调用公用拦截器
 *
 * @author:zhangyongbin5
 * @date:2021/9/23
 */
@Slf4j
public class MyBaseFeignInterceptor extends BaseFeignInterceptor {

    @Override
    protected void customer(RequestTemplate requestTemplate) {
        requestTemplate.header("APP","common");
    }

}
