package com.bruce.common.log;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.List;
import java.util.ServiceLoader;

/**
 * ConversionRule
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/28
 */
public class ConversionRule extends MessageConverter {

    private static List<String> keys;

    private static ServiceLoader<LogSensitiveConverter> services;

    public static void setKeys(List<String> keys) {
        ConversionRule.keys = keys;
    }

    /**
     *
     * @return
     */
    public ServiceLoader<LogSensitiveConverter> loadServices() {
        if (services == null) {
            services = ServiceLoader.load(LogSensitiveConverter.class);
        }
        return services;
    }

    @Override
    public String convert(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        for (LogSensitiveConverter service : loadServices()) {
            if (keys != null && keys.contains(service.getKey())) {
                message = service.convert(message);
            }
        }
        return message;
    }

}
