package com.bright.apollo.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.bright.apollo.service.impl.UserDetailsServiceImpl;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月2日  
 *@Version:1.1.0  
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;
    @Autowired
    private WebResponseExceptionTranslator customWebResponseExceptionTranslator;

    @Bean
    RedisTokenStore redisTokenStore(){
        return new RedisTokenStore(jedisConnectionFactory);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
//    	 clients.inMemory()
//         .withClient("webApp")
//         .authorizedGrantTypes("authorization_code")
//         .authorities("ROLE_USER")
//         .scopes("write")
//         .resourceIds("oauth2")
//         .secret("webApp")
//         .autoApprove(true);
    }
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    	logger.info("====== AuthorizationServerEndpointsConfigurer endpoints =====");
        endpoints.tokenStore(redisTokenStore())
                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())
//                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
        endpoints.tokenServices(defaultTokenServices());
        endpoints.exceptionTranslator(customWebResponseExceptionTranslator);
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(redisTokenStore());
        tokenServices.setSupportRefreshToken(true);
        logger.info(" ======================================= defaultTokenServices ====================== ");

//        tokenServices.setClientDetailsService(clientDetails());

        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 24 * 7); // token 7 day
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 30);//refresh token 30 day
        return tokenServices;
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
        security .checkTokenAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients();
    }
 
}
