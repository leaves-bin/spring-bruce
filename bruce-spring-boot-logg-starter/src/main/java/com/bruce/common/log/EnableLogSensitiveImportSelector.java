package com.bruce.common.log;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.type.AnnotationMetadata;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * EnableLogSensitiveImportSelector
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2021/9/28
 */
@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class EnableLogSensitiveImportSelector implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> enableAttrMap = metadata.getAnnotationAttributes(EnableLogSensitive.class.getName());
        AnnotationAttributes enableAttrs = AnnotationAttributes.fromMap(enableAttrMap);
        String keys = enableAttrs.getString("keys");

        ConfigurableEnvironment configEnv = (ConfigurableEnvironment) environment;
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put(LogSensitiveConverterAutoConfiguration.PREFIX + ".enabled", true);
        map.put(LogSensitiveConverterAutoConfiguration.PREFIX + ".keys", keys);
        MapPropertySource propertySource = new MapPropertySource("logSensitiveConverterAutoConfiguration", map);
        configEnv.getPropertySources().addLast(propertySource);
    }

}
