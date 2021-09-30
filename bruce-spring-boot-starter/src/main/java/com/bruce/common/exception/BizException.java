package com.bruce.common.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * BizException
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/27
 */
@Getter
@ToString
public class BizException extends RuntimeException {
    /**
     * 异常编码
     */
    private Object errCode;
    /**
     * 异常信息
     */
    private String errMsg;
    /**
     * 异常参数
     */
    private Object[] args;

    public BizException(Object errCode, String errMsg, Object... args) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.args = args;
    }

    public BizException(Object errCode, String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public static BizException of(Object errCode, String errMsg, Object... args) {
        return new BizException(errCode, errMsg, args);
    }

    public static BizException of(ErrorCodeEnum errCode) {
        return new BizException(errCode.getCode(), errCode.getDescription());
    }

    public static BizException of(ErrorCodeEnum errCode, Throwable cause) {
        return new BizException(errCode.getCode(), errCode.getDescription(), cause);
    }

    public BizException(ErrorCodeEnum errCode, Throwable cause) {
        super(errCode.getDescription(), cause);
        this.errCode = errCode.getCode();
        this.errMsg = errCode.getDescription();
    }

    public BizException(ErrorCodeEnum errCode, String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
