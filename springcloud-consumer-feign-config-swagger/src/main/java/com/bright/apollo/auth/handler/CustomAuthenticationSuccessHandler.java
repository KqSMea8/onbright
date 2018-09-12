package com.bright.apollo.auth.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bright.apollo.util.AuthUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 认证成功处理器
 *
 * @author ： CatalpaFlat
 * @date ：Create in 21:31 2017/12/20
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class.getName());
    public static final String BASIC_ = "Basic ";
    @Value("${response.type}")
    public String responseType;
    @Autowired
    @Lazy
    private ClientDetailsService clientDetailsService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    @Lazy
    private AuthorizationServerTokenServices authorizationServerTokenServices;
	 
    public CustomAuthenticationSuccessHandler() {
        logger.info("CustomAuthenticationSuccessHandler loading ...");
    }

    /**
     * 登录成功被调用
     */
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
         /*
         * authentication:封装认证信息（用户信息等）
         */
        logger.info("Authentication success");
 
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BASIC_)) {
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
        }
        try {
            String[] tokens = AuthUtils.extractAndDecodeHeader(header);
            assert tokens.length == 2;
            String  clientId = tokens[0];

            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

            //校验secret
            if (!clientDetails.getClientSecret().equals(tokens[1])) {
                throw new InvalidClientException("Given client ID does not match authenticated client");
            }
            HashMap<String, String> hashMap = new HashMap<String,String>();
            TokenRequest tokenRequest = new TokenRequest(hashMap, 
           		 clientId, clientDetails.getScope(), "mobile");
            //校验scope
            new DefaultOAuth2RequestValidator().validateScope(tokenRequest, clientDetails);
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            logger.info("获取token 成功：{}", oAuth2AccessToken.getValue());

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.append(objectMapper.writeValueAsString(oAuth2AccessToken));
        } catch (IOException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }
    }
}
