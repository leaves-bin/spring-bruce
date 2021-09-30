package com.bruce.business.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ApiTestController
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/22
 */
@Slf4j
@RestController
@RequestMapping("/common/api")
public class ApiTestController {

    @GetMapping("/test")
    public String test(String token, String code) {
        log.info("code = {} , token = {}", code, token);
        return "success";
    }

}
