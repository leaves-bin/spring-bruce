package com.bruce.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * RouterLocatorConfig
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/23
 */
@Configuration
public class RouterLocatorConfig {

    @Bean(name = "redisRateLimiter")
    public RateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 2);
    }


    @Bean(name = "limiterKeyResolver")
    public KeyResolver limiterKeyResolver() {
        //请求中某个参数
        //return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
        //ip
        //return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
        //根据uri限流
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

}
