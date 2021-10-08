package com.bruce.common.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bruce.common.exception.ErrorCodeEnum;
import com.bruce.common.utils.BeanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.slf4j.MDC;

import java.io.Serializable;
import java.util.Optional;

/**
 * RespData
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/27
 */
@EqualsAndHashCode
@ToString
@Data
public final class RespData<T> implements Serializable {

    public static final String SUCCESS_CODE = "0000";
    public static final String SUCCESS_MSG = "success";
    public static final String ERROR_CODE = "10000";
    public static final String ERROR_MSG = "error";

    /**
     * 状态码
     */
    private String code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 日志链路
     */
    private String traceId;
    /**
     * 时间戳
     */
    private long timestamp;

    public RespData() {
    }

    private RespData(String code, String message) {
        this.code = code;
        this.message = message;
        this.traceId = MDC.get("X-B3-TraceId");
        this.timestamp = System.currentTimeMillis();
    }

    private RespData(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.traceId = MDC.get("X-B3-TraceId");
        this.timestamp = System.currentTimeMillis();
        this.data = data;
    }

    public static <T> RespData<T> of(ErrorCodeEnum errCode) {
        return of(errCode, null);
    }

    public static <T> RespData<T> of(ErrorCodeEnum errCode, T data) {
        return of(errCode.getCode(), errCode.getDescription(), data);
    }

    public static <T> RespData<T> of(ErrorCodeEnum errCode, String message, T data) {
        return of(errCode.getCode(), Optional.ofNullable(message).orElse(errCode.getDescription()), data);
    }

    public static <T> RespData<T> of(String code, String message, T data) {
        return new RespData(code, message, data);
    }

    public static <T> RespData<T> success(T data) {
        return of(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static RespData success() {
        return success(null);
    }

    public static <T> RespData<T> error(ErrorCodeEnum errCode) {
        return of(errCode.getCode(), errCode.getDescription(), null);
    }

    public static <T> RespData<T> error() {
        return of(ERROR_CODE, ERROR_MSG, null);
    }
}
