###Spring Cloud Gateway 和Zuul的区别

1. Spring Cloud中集成的Zuul版本，采用的是Tomcat容器，使用传统的Servlet IO处理模型，是一种阻塞式IO模型。
2. Spring Cloud Gateway使用的是Webflux中的reactor-netty响应式编程组件，底层使用Netty作为通讯框架,Netty是目前业界认可最高性能通讯框架。

###Spring Cloud Gateway 三大术语

1. Filter (过滤器：org.springframework.cloud.gateway.filter.GatewayFilter) 
顾名思义，可以用它对请求进行拦截和修改，进行二次处理
2. Predicate (断言)
3. Route (路由)
一个路由会包含一个id，一个uri,一组过滤器，一组断言
   
###路由配置
1. 单个uri配置，访问地址以 /spring 或者 /project 开头 将会路由到 https://spring.io/projects
    ````
    spring.cloud.gateway.routes[0].id = default_route
    spring.cloud.gateway.routes[0].uri = https://spring.io/projects
    spring.cloud.gateway.routes[0].predicates[0] = Path=spring,project
    ````

2. 微服务地址路由，在uri协议部分用lb标识微服务调用
    ````
    #eureka 服务注册
    eureka.instance.prefer-ip-address = true
    eureka.client.service-url.defaultZone = http://localhost:8901/eureka
    
    spring.cloud.gateway.routes[1].id = bruce_route
    spring.cloud.gateway.routes[1].uri = lb://bruce-common
    spring.cloud.gateway.routes[1].predicates[0] = Path=/common/**
    spring.cloud.gateway.routes[1].predicates[1].name = ReadBodyPredicateFactory
    ````

3. Predicate (断言)条件匹配
   
    Spring Cloud Gateway 内置了几种 Predicate 用于条件匹配
    ````
    After: AfterRoutePredicateFactory, 请求时间在配置时间之后
    Before: BeforeRoutePredicateFactory, 请求时间在配置时间之前
    Between: BetweenRoutePredicateFactory, 请求时间在配置时间之间
    Cookie: CookieRoutePredicateFactory, Cookie正则匹配
    Header: HeaderRoutePredicateFactory, Header正则匹配
    Host: HostRoutePredicateFactory, Host匹配
    Method: MethodRoutePredicateFactory, 请求方法匹配
    Path: PathRoutePredicateFactory, 请求路径匹配
    Query: QueryRoutePredicateFactory, 请求参数正则匹配
    ReadBodyPredicateFactory: QueryRoutePredicateFactory, 请求参数正则匹配
    RemoteAddr: RemoteAddrRoutePredicateFactory, 远程地址匹配
    Weight: WeightRoutePredicateFactory, 权重匹配
    CloudFoundryRouteService: CloudFoundryRouteServiceRoutePredicateFactory, 云计算路由匹配
    ````
4. Filter 过滤规则

   org.springframework.cloud.gateway.filter.GatewayFilter 的实现类前缀为规则名称，如 AddRequestHeaderGatewayFilterFactory ，过滤规则为AddRequestHeader，配置方法为
   ````
   spring.cloud.gateway.routes[1].filters[0] = AddRequestHeader=token,testToken
   ````
   其他的过滤还有 AddRequestParameter 、 AddResponseHeader 、 RewritePath 、 SetPath等

5. 通过代码进行配置
   
   我们在项目启动时会发现日志中有关于路由的加载
   ````
   10:30:39.986, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [After] 
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Before]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Between]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Cookie]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Header]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Host]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Method]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Path]
   10:30:39.987, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Query]
   10:30:39.988, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [ReadBodyPredicateFactory]
   10:30:39.988, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [RemoteAddr]
   10:30:39.988, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [Weight]
   10:30:39.988, [ ] [main] INFO  [] o.s.c.g.r.RouteDefinitionRouteLocator lambda$initFactories$2:139 - Loaded RoutePredicateFactory [CloudFoundryRouteService]
   ````
   定位于 initFactories 方法可以看到是在声明 RouteLocator bean 的时候进行的路由规则解析，由此，我们可以在声明 bean RouteLocator 的时候进行配置。
   ````
   @Bean
    public RouteLocator routeDefinitionRouteLocator(GatewayProperties properties,
			List<GatewayFilterFactory> gatewayFilters,
			List<RoutePredicateFactory> predicates,
			RouteDefinitionLocator routeDefinitionLocator,
			ConfigurationService configurationService) {
		return new RouteDefinitionRouteLocator(routeDefinitionLocator, predicates,
				gatewayFilters, properties, configurationService);
	}
   ````
   代码进行配置,声明自定义路由
   ````
    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("test_router",pre->pre.path("/baidu").uri("http://www.baidu.com"))
                .build();
    }
   ````
###熔断降级
网关作为统一请求入口，所有的请求都会通过网关做响应的路由转发，此时不可避免会出现调用失败、调用超时等情况，此时请求会堆积在网关，造成其他请求排队等待，在大规模并发情况下会导致整个系统瘫痪。因此必须在网关层面做熔断、降级处理，快速返回失败给客户端。
````
spring.cloud.gateway.routes[1].filters[0].name = Hystrix
spring.cloud.gateway.routes[1].filters[0].args.name = fallbackCmdWait
spring.cloud.gateway.routes[1].filters[0].args.fallbackUri = forward:/fallbackWait

hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds = 5000
hystrix.command.fallbackCmdWait.execution.isolation.thread.timeoutInMilliseconds = 5000


@RestController
public class FallbackController {

    @RequestMapping("/fallbackWait")
    public JSONObject fallbackWait() {
        return new JSONObject() {{
            put("code", "500");
            put("message", "服务不可用");
            put("data", null);
        }};
    }

}
````
Hystrix 为 HystrixGatewayFilterFactory 的名称

fallbackCmdWait 是自定义的熔断名称

fallbackUri 是自定义的熔断地址，这里需要增加熔断降级后统一返回的url

###网关限流
````
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
````
Gateway限流依赖redis来实现令牌桶算法
````
spring.cloud.gateway.routes[1].filters[2].name = RequestRateLimiter
spring.cloud.gateway.routes[1].filters[2].args.key-resolver = #{@limiterKeyResolver}
spring.cloud.gateway.routes[1].filters[2].args.redis-rate-limiter.replenishRate = 1
spring.cloud.gateway.routes[1].filters[2].args.redis-rate-limiter.burstCapacity = 2
````

###路由原理
   spring的请求首先会通过 HandlerMappingIntrospector 进行拦截匹配合适的处理器进行处理,由此通过 AbstractHandlerMapping 的getHandler()来选择处理器,Gateway 自动装配的时候会将Gateway的 RoutePredicateHandlerMapping 进行Bean 注入，通过该Bean的 getHandlerInternal  方法实现路由功能
   ````
   @Override
    protected Mono<?> getHandlerInternal(ServerWebExchange exchange) {
        // don't handle requests on management port if set and different than server port
        if (this.managementPortType == DIFFERENT && this.managementPort != null
                && exchange.getRequest().getURI().getPort() == this.managementPort) {
            return Mono.empty();
        }
        exchange.getAttributes().put(GATEWAY_HANDLER_MAPPER_ATTR, getSimpleName());
        //
        return lookupRoute(exchange)
                // .log("route-predicate-handler-mapping", Level.FINER) //name this
                .flatMap((Function<Route, Mono<?>>) r -> {
                    exchange.getAttributes().remove(GATEWAY_PREDICATE_ROUTE_ATTR);
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                                "Mapping [" + getExchangeDesc(exchange) + "] to " + r);
                    }
   
                    exchange.getAttributes().put(GATEWAY_ROUTE_ATTR, r);
                    return Mono.just(webHandler);
                }).switchIfEmpty(Mono.empty().then(Mono.fromRunnable(() -> {
                    exchange.getAttributes().remove(GATEWAY_PREDICATE_ROUTE_ATTR);
                    if (logger.isTraceEnabled()) {
                        logger.trace("No RouteDefinition found for ["
                                + getExchangeDesc(exchange) + "]");
                    }
                })));
    }
    
    protected Mono<Route> lookupRoute(ServerWebExchange exchange) {
		return this.routeLocator.getRoutes()
				// individually filter routes so that filterWhen error delaying is not a
				// problem
				.concatMap(route -> Mono.just(route).filterWhen(r -> {
					// add the current route we are testing
					exchange.getAttributes().put(GATEWAY_PREDICATE_ROUTE_ATTR, r.getId());
					return r.getPredicate().apply(exchange);
				})
						// instead of immediately stopping main flux due to error, log and
						// swallow it
						.doOnError(e -> logger.error(
								"Error applying predicate for route: " + route.getId(),
								e))
						.onErrorResume(e -> Mono.empty()))
				// .defaultIfEmpty() put a static Route not found
				// or .switchIfEmpty()
				// .switchIfEmpty(Mono.<Route>empty().log("noroute"))
				.next()
				// TODO: error handling
				.map(route -> {
					if (logger.isDebugEnabled()) {
						logger.debug("Route matched: " + route.getId());
					}
					validateRoute(route, exchange);
					return route;
				});

		/*
		 * TODO: trace logging if (logger.isTraceEnabled()) {
		 * logger.trace("RouteDefinition did not match: " + routeDefinition.getId()); }
		 */
	}
   ````