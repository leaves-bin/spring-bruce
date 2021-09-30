package com.bruce.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhoneLogSensitiveConverter
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/28
 */
public class PhoneLogSensitiveConverter implements LogSensitiveConverter {

    public static final Logger LOGGER = LoggerFactory.getLogger(PhoneLogSensitiveConverter.class);

    private static final Pattern PATTERN = Pattern.compile("(?<!\\d)?1[3578]\\d{9}(?!\\d)");

    @Override
    public String getKey() {
        return "phone";
    }

    @Override
    public String convert(String message) {
        try {
            Matcher matcher = PATTERN.matcher(message);
            while (matcher.find()) {
                String phone = matcher.group(0);
                message = convert(message, phone);
            }
        } catch (Exception e) {
            LOGGER.warn("log convert [phone] warn {}", e.getMessage(), e);
        }
        return message;
    }

    /**
     * 正则替换
     * @param message
     * @param phone
     * @return
     */
    private String convert(String message, String phone) {
        if (phone != null) {
            String newValue = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1***$2");
            message = message.replaceAll(phone, newValue);
        }
        return message;
    }

}
