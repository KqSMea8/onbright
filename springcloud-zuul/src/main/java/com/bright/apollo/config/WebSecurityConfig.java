package com.bright.apollo.config;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import com.bright.apollo.service.impl.UserDetailsServiceImpl;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月2日  
 *@Version:1.1.0  
 */
@Order(10)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

  /*  @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
            //.passwordEncoder(passwordEncoder()); encrypt
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.csrf().disable()
            .requestMatchers().anyRequest().and()
            .authorizeRequests().antMatchers("/health", "/css/**","/oauth/**","/uaa/**").permitAll()
            .and()
            .formLogin().loginPage("/login")
//            .successForwardUrl("/aouth2/sendRedirect")
            .permitAll();
//            .and()
//            .addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ioc","/authorization/*");
    }
    
//    public class BeforeLoginFilter extends GenericFilterBean {
//
//        @Override
//        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//            logger.info("This is a filter before UsernamePasswordAuthenticationFilter.");
//            HttpServletRequest request = (HttpServletRequest)servletRequest;
//            String redirect_uri = request.getParameter("redirect_uri");
//            String state = request.getParameter("state");
//            logger.info(request.getRequestURI());
//            if(redirect_uri!=null){
//            	redirect_uri = URLDecoder.decode(redirect_uri, "UTF-8");
//            	redirect_uri += "&"+state;
//            	logger.info(redirect_uri);
//            	request.setAttribute("redirect_uri", redirect_uri);
//            }
//            Enumeration<String> m = request.getParameterNames();
//            while(m.hasMoreElements()){
//            	String element = m.nextElement();
//            	logger.info("params ------ "+element+" ------ "+request.getParameter(element));
//            }
//
//            // 继续调用 Filter 链
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//    }
    

}
