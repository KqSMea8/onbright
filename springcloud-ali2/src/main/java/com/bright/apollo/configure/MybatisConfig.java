//package com.bright.apollo.configure;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//public class MybatisConfig {
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//        public DataSource dataSource(){return new com.alibaba.druid.pool.DruidDataSource();
//    }
//
//    @Bean(name="sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory() throws  Exception{
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        DataSource dataSource = dataSource();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        return sqlSessionFactoryBean.getObject();
//    }
//}
