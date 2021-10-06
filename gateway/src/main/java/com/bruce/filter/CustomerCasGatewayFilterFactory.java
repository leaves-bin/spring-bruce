package com.bruce.filter;

import com.bruce.common.GatewayErrorCodeEnum;
import com.bruce.common.GatewayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * CustomerCasGatewayFilterFactory
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/30
 */
@Slf4j
@Component
public class CustomerCasGatewayFilterFactory extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest req = exchange.getRequest();
                addOriginalRequestUrl(exchange, req.getURI());
                String path = req.getURI().getRawPath();

                log.info("=====================CustomerCasGatewayFilterFactory==========================");
                HttpHeaders headers = req.getHeaders();
                for (Map.Entry<String, List<String>> stringListEntry : headers.entrySet()) {
                    log.info(stringListEntry.getKey() + " = " + stringListEntry.getValue());

                }
                log.info(path);

                log.info("=====================CustomerCasGatewayFilterFactory==========================");

                List<String> token = headers.get("Token");
                if (CollectionUtils.isEmpty(token)) {
                    throw new GatewayException(GatewayErrorCodeEnum.AUTH_ERROR);
                }

                return chain.filter(exchange);
            }

            @Override
            public String toString() {
                return filterToStringCreator(CustomerCasGatewayFilterFactory.this).toString();
            }
        };
    }
}
