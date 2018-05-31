//package com.bright.apollo.configure;
//
//import org.mybatis.spring.annotation.MapperScan;
//import org.mybatis.spring.mapper.MapperScannerConfigurer;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@AutoConfigureAfter(MybatisConfig.class)
//@MapperScan("com.bright.apollo.dao.*")
//public class MybatisMapperScannerConfig {
//    public MapperScannerConfigurer mapperScannerConfigurer(){
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        mapperScannerConfigurer.setBasePackage("com.bright.apollo.dao.*");
//        return mapperScannerConfigurer;
//    }
//}
