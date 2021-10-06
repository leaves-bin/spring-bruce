package com.bruce.common;

import lombok.Data;

/**
 * GatewayExcept
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/30
 */
@Data
public class GatewayException extends RuntimeException {
    /**
     * 异常编码
     */
    private String errCode;
    /**
     * 异常信息
     */
    private String errMsg;

    public GatewayException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public GatewayException(GatewayErrorCodeEnum error) {
        super(error.getDescription());
        this.errCode = error.getCode();
        this.errMsg = error.getDescription();
    }

}