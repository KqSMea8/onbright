package com.bright.apollo.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 自定义失败处理器
 *
 * @author ： CatalpaFlat
 * @date ：Create in 21:17 2017/12/20
 */
@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class.getName());

    @Value("${response.type}")
    public String responseType;

    @Autowired
    private ObjectMapper objectMapper;

    public CustomAuthenticationFailureHandler() {
        logger.info("CustomAuthenticationFailureHandler loading ...");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        /*
         * exception：错误信息（认证过程中的错误）
         */
        logger.error("Authentication does not pass");

        if (StringUtils.equals("jspn", responseType)) {
            int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ResponseObject res =new ResponseObject();
            res.setStatus(ResponseEnum.AuthenticationError.getStatus());
			res.setMessage(ResponseEnum.AuthenticationError.getMsg());
            response.getWriter().write(objectMapper.writeValueAsString(res));
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
