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

//    @Autowired
//    private RedisBussines redisBussines;
//
//    @Autowired
//    private UserService userService;


//    @Autowired
//    private MqttPahoMessageDrivenChannelAdapter adapter;

  /*  @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
            //.passwordEncoder(passwordEncoder()); 
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.csrf().disable()
            .requestMatchers().anyRequest().and()
            .authorizeRequests().antMatchers("/health", "/css/**","/oauth/**","/uaa/**").permitAll()
            .and()
            .formLogin().loginPage("/login").permitAll();
//            .and()
//            .addFilterAfter(new AfterLoginFilter(),UsernamePasswordAuthenticationFilter.class);
//            .successForwardUrl("/aouth2/sendRedirect")

//            .and()
//            .addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ioc","/authorization/*");
    }

//    public class AfterLoginFilter extends GenericFilterBean {
//
//        @Override
//        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//            logger.info("This is a filter after UsernamePasswordAuthenticationFilter.");
//            HttpServletRequest request = (HttpServletRequest)servletRequest;
//
//            Enumeration<String> m = request.getParameterNames();
//            while(m.hasMoreElements()){
//                String element = m.nextElement();
//                logger.info("params ------ "+element+" ------ "+request.getParameter(element));
//            }
//
//            String appKey = request.getParameter("appkey");
//            String accessToken = request.getParameter("access_token");
//            if(!StringUtils.isEmpty(appKey)&&!StringUtils.isEmpty(accessToken)){//非第三方登录
//                OAuth2Authentication defaultOAuth2AccessToken = redisBussines.getObject("auth:"+accessToken,OAuth2Authentication.class);
//                User user = (User)defaultOAuth2AccessToken.getPrincipal();
//                String userName = user.getUsername();
//                userDetailsService.loadUserByUsername(user.getUsername());
//                TUser tUser =null;
//                if(NumberHelper.isNumeric(userName)){
//                    tUser =userService.queryUserByName(userName);
//                }else{
//                    tUser =userService.queryUserByOpenId(userName);
//                }
//                Integer userId = tUser.getId();
//                String appKeyUserId = redisBussines.get("appkey_userId"+userId);
//                if(appKeyUserId==null||StringUtils.isEmpty(appKeyUserId)){
//                    redisBussines.setValueWithExpire("appkey_userId"+userId,appKey,60 * 60 * 24 * 7);
//                }else if(appKeyUserId !=null && appKey!=null && !appKeyUserId.equals(appKey)){
//                    String[] appKeyUserIdArr = appKeyUserId.split(":");
//                    logger.info("redis appKeyUserId "+appKeyUserId);
//                    logger.info(" appKey "+appKey);
//                    for(int i=0;i<appKeyUserIdArr.length;i++){
//                        if(!appKey.equals(appKeyUserIdArr[i])){
//                            redisBussines.setValueWithExpire("appkey_userId"+userId,appKeyUserIdArr[i]+":"+appKey,60 * 60 * 24 * 7);
//                        }
//                    }
//                    logger.info("------ appKeyUserId ======= "+redisBussines.get("appkey_userId"+userId));
//
//                }
//                appKeyUserId = redisBussines.get("appkey_userId"+userId);
//                String[] appkeyUserIdArr = appKeyUserId.split(":");
//                logger.info("------ appKeyUserId -----"+appKeyUserId);
//                String topicName = "";
//                boolean isExists = false;
//                if(appKeyUserId != null){
//                    for(int i=0;i<appkeyUserIdArr.length;i++){
//                        String[] topics = adapter.getTopic();
//                        topicName = "ob-smart."+appkeyUserIdArr[i];
//                        logger.info(" ====== topicName ======"+topicName);
//                        for(int j=0;j<topics.length;j++){
//                            if(topics[j].equals(topicName)){
//                                isExists=true;
//                            }
//                        }
//                        if(isExists==false){
//                            logger.info(" ======= create topic ======= ");
//                            try{
//                                adapter.addTopic("ob-smart."+appkeyUserIdArr[i],1);
//                            }catch (Exception e){
//                                logger.info("====== create topic exception ====== "+e.getMessage());
//                            }
//                        }
//                        isExists=false;
//                    }
//                }
//            }
//
//
//            // 继续调用 Filter 链
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//    }
     
    public class BeforeLoginFilter extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            logger.info("This is a filter before UsernamePasswordAuthenticationFilter.");
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            String redirect_uri = request.getParameter("redirect_uri");
            String state = request.getParameter("state");
            logger.info(request.getRequestURI());
            if(redirect_uri!=null){
            	redirect_uri = URLDecoder.decode(redirect_uri, "UTF-8");
            	redirect_uri += "&"+state;
            	logger.info(redirect_uri);
            	request.setAttribute("redirect_uri", redirect_uri);
            }
            Enumeration<String> m = request.getParameterNames();
            while(m.hasMoreElements()){
            	String element = m.nextElement();
            	logger.info("params ------ "+element+" ------ "+request.getParameter(element));
            }

            // 继续调用 Filter 链
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    

}
