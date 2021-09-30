package com.bruce.common.exception;

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
public enum ErrorCodeEnum {
    BIZ_ERROR("1001", "业务异常"),

    VALIDATE("1002", "参数校验异常"),

    IO_ERROR("1003", "IO异常"),

    TIME_OUT("1004", "业务超时"),

    DATA_NOT_EXIST("1005", "数据不存在"),

    DATA_ERROR("1006", "数据错误"),

    DATA_ACCESS_ERROR("1007", "数据访问异常"),

    NO_PRIVILEGE("1008", "权限不足"),

    ENCRYPT_DECRYPT_ERROR("1009", "加解密失败"),

    DATE_PARSER_ERROR("1010", "日期解析错误"),

    UNKNOWN("1011", "未知异常"),

    REPEAT_REQ("1012", "重复请求"),
    ;

    private String code;
    private String description;


    public static ErrorCodeEnum get(String code) {
        for (ErrorCodeEnum value : ErrorCodeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
