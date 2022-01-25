package com.bruce.search.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;

/**
 * DataSourceConfig
 * 类描述：
 *
 * @author:zhangyongbin5
 * @date:2022/1/19
 */
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = {"com.bruce.search.biz.mapper"})
public class DataSourceConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath*:mybatis/**/*.xml"));

        // 分页插件
        Properties properties = new Properties();
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");

        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor(DbType.ORACLE);
        interceptor.setMaxLimit(10000L);
        interceptor.setProperties(properties);

        MybatisPlusInterceptor plusInterceptor = new MybatisPlusInterceptor();
        plusInterceptor.addInnerInterceptor(interceptor);
        sqlSessionFactory.setPlugins(plusInterceptor);
        sqlSessionFactory.setGlobalConfig(globalConfig());

        return sqlSessionFactory.getObject();
    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig conf = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setKeyGenerators(Arrays.asList(new OracleKeyGenerator()));
        dbConfig.setIdType(IdType.INPUT);
        conf.setDbConfig(dbConfig);
        return conf;
    }
}
