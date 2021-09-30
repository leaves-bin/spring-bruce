package com.bruce.common.log;

/**
 * LogSensitiveConverter
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/28
 */
public interface LogSensitiveConverter {
    /**
     * 别名
     *
     * @return
     */
    String getKey();

    /**
     * 转换
     *
     * @param message:日志体
     * @return
     */
    String convert(String message);
}
