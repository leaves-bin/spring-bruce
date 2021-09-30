package com.bruce.common.interceptor;

import com.alibaba.fastjson.JSON;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * BaseFeignInterceptor
 * 类描述：feign调用公用拦截器
 *
 * @author:zhangyongbin5
 * @date:2021/9/23
 */
@Slf4j
public class BaseFeignInterceptor implements RequestInterceptor {

    public BaseFeignInterceptor() {
    }

    /**
     * 自定义请求书属性配置：eg:  requestTemplate.header("AppName", "app");
     * @param requestTemplate
     */
    protected void customer(RequestTemplate requestTemplate) {

    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        this.customer(requestTemplate);
        log.info("feign header:{}", JSON.toJSONString(requestTemplate.headers()));
    }

}
