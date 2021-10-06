package com.bruce.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BizExceptionCodeEnum
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/27
 */
@Getter
@AllArgsConstructor
public enum GatewayErrorCodeEnum {
    AUTH_ERROR("001", "权限认证失败"),

    ;

    private String code;
    private String description;


    public static GatewayErrorCodeEnum get(String code) {
        for (GatewayErrorCodeEnum value : GatewayErrorCodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
