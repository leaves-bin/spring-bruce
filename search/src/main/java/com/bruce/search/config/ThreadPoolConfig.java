package com.bruce.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: yongbin
 * @Date: 2019-09-03
 * @Desc: <pre></pre>
 */
@Configuration
public class ThreadPoolConfig {

    private final int CORE_POOL_SIZE = 5;
    private final int MAX_POOL_SIZE = 10;
    private final int KEEP_ALIVE_SECONDS = 60;
    private final int QUEUE_CAPACITY = 5000;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setAllowCoreThreadTimeOut(false);
        executor.setThreadNamePrefix("hep-thread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.initialize();
        return executor;
    }

}
