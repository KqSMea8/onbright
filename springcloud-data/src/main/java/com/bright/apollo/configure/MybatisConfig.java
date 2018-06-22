package com.bright.apollo.configure;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.driver-class-name}")
    private String driverName;

    @Value("${datasource.username}")
    private String userName;

    @Value("${datasource.password}")
    private String pwd;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring:datasource")
        public DruidDataSource dataSource(){return new com.alibaba.druid.pool.DruidDataSource();
    }

    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws  Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        DruidDataSource dataSource = dataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverName);
        dataSource.setUsername(userName);
        dataSource.setPassword(pwd);
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }
}
