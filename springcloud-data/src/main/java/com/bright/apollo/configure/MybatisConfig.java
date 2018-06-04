package com.bright.apollo.configure;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan
@Configuration
public class MybatisConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring:datasource")
        public DruidDataSource dataSource(){return new com.alibaba.druid.pool.DruidDataSource();
    }

    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws  Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        DruidDataSource dataSource = dataSource();
        dataSource.setUrl("jdbc:mysql://rm-bp16t7i071284ljuono.mysql.rds.aliyuncs.com:3306/onbright_ali_new?useUnicode=true&amp;characterEncoding=utf8");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("Iltwao!1");
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }
}
