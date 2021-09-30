package com.bruce.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * LogSensitiveConverterAutoConfiguration
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/28
 */
@Configuration
public class LogSensitiveConverterAutoConfiguration implements EnvironmentAware {

    public static final Logger LOGGER = LoggerFactory.getLogger(PhoneLogSensitiveConverter.class);

    public static final String PREFIX = "yanxi.log.sensitive";

    @Override
    public void setEnvironment(Environment environment) {
        ConversionRule.setKeys(getKeys(environment));
    }

    /**
     *  解析设置的 keys
     * @param environment
     * @return
     */
    private static List<String> getKeys(final Environment environment) {
        String enabled = environment.getProperty(PREFIX + ".enabled", "false");
        if (Boolean.parseBoolean(enabled)) {
            String property = environment.getProperty(PREFIX + ".keys", "");
            if (property == null || property.equals("")) {
                LOGGER.warn("open log sensitive enabled , but not set keys");
            } else {
                return Arrays.asList(StringUtils.commaDelimitedListToStringArray(property));
            }
        }
        return null;
    }
}
