package com.bright.apollo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bright.apollo.config.service.impl.UserDetailsServiceImpl;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/**
 *@Title:
 *@Description:
 *@Author:JettyLiu
 *@Since:2018年3月7日
 *@Version:1.1.0
 */
@Order(10)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
            //.passwordEncoder(passwordEncoder()); encrypt
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.csrf().disable()
            .requestMatchers().anyRequest().and()
            .authorizeRequests().antMatchers("/health", "/css/**","/oauth/**").permitAll()
            .and()
            .formLogin().loginPage("/login")
            .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ioc","/authorization/*");
    }

}
