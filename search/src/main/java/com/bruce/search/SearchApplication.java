package com.bruce.search;

import com.bruce.common.log.EnableLogSensitive;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CommonApplication
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/22
 */
@EnableLogSensitive(keys = "phone")
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

}
