package com.bright.apollo.config;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

import com.bright.apollo.config.service.impl.UserDetailsServiceImpl;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;


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
            .successForwardUrl("/aouth2/sendRedirect")
            .permitAll()
            .and()
            .addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ioc","/authorization/*");
    }
    
    public class BeforeLoginFilter extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            System.out.println("This is a filter before UsernamePasswordAuthenticationFilter.");
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            String redirect_uri = request.getParameter("redirect_uri");
            String state = request.getParameter("state");
            System.out.println(request.getRequestURI());
            if(redirect_uri!=null){
            	redirect_uri = URLDecoder.decode(redirect_uri, "UTF-8");
            	redirect_uri += "&"+state;
            	System.out.println(redirect_uri);
            	request.setAttribute("redirect_uri", redirect_uri);
            }
            Enumeration<String> m = request.getParameterNames();
            while(m.hasMoreElements()){
            	String element = m.nextElement();
            	System.out.println("params ------ "+element+" ------ "+request.getParameter(element));
            }
            
            // 继续调用 Filter 链
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    

}
