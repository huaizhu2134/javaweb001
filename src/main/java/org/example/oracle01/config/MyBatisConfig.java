package org.example.oracle01.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@MapperScan("org.example.oracle01.mapper")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        
        // 设置Mapper XML文件的位置
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:mapper/*.xml");
        sessionFactory.setMapperLocations(resources);
        
        // 设置类型别名包
        sessionFactory.setTypeAliasesPackage("org.example.oracle01.model");
        
        return sessionFactory.getObject();
    }
}