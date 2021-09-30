package com.bruce.fallback;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FallbackController
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/23
 */
@RestController
public class FallbackController {

    @RequestMapping("/systemMaintenance")
    public JSONObject fallbackWait() {
        return new JSONObject() {{
            put("code", "505");
            put("message", "系统维护中");
            put("data", null);
        }};
    }

    @RequestMapping("/commonFallback")
    public JSONObject commonFallback() {
        return new JSONObject() {{
            put("code", "500");
            put("message", "服务不可用");
            put("data", null);
        }};
    }



}
