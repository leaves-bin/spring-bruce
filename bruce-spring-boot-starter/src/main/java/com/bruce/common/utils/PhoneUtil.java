package com.bruce.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * PhoneUtil
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/10/6
 */
@Slf4j
public class PhoneUtil {

    /**
     * 手机号码正则
     */
    public static String MOBILE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";

    /**
     * 手机号码正则
     */
    public static final Pattern mobilePattern = Pattern.compile(MOBILE_REGEX);

    /**
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobile(String phoneNum) {
        String regex = "(\\+\\d+)?1[3456789]\\d{9}$";
        if (Pattern.matches(regex, phoneNum)) {
            return true;
        } else {
            log.warn("{} 不符合手机号码规则", phoneNum);
            return false;
        }
    }

}
