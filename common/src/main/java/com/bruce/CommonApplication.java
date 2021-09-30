package com.bruce;

import com.bruce.common.log.EnableLogSensitive;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * CommonApplication
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/22
 */
@EnableLogSensitive(keys = "phone")
@SpringBootApplication
@EnableDiscoveryClient
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

}
