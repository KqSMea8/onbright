package com.bright.apollo.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.token.OAuthToken;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    Logger logger = Logger.getLogger(AuthorizationController.class);

    private  String getIpAddr(HttpServletRequest request)  {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        System.out.println(" ====== Xip ====== "+ Xip);
        System.out.println(" ====== XFor ====== "+ XFor);
        System.out.println(" ====== Proxy-Client-IP ====== "+ request.getHeader("Proxy-Client-IP"));
        System.out.println(" ====== WL-Proxy-Client-IP ====== "+ request.getHeader("WL-Proxy-Client-IP"));
        System.out.println(" ====== HTTP_CLIENT_IP ====== "+ request.getHeader("HTTP_CLIENT_IP"));
        System.out.println(" ====== HTTP_X_FORWARDED_FOR ====== "+ request.getHeader("HTTP_X_FORWARDED_FOR"));
        System.out.println(" ====== getRemoteAddr ====== "+ request.getRemoteAddr());
        System.out.println(" ====== Host ====== "+ request.getHeader("Host"));

        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = XFor.indexOf(",");
            if(index != -1){
                return XFor.substring(0,index);
            }else{
                return XFor;
            }
        }
        XFor = Xip;
        System.out.println(" ====== XFor = Xip ====== "+ XFor);
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)){
            System.out.println(" ====== unKnown ====== ");
            return XFor;
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            System.out.println(" ====== Proxy-Client-IP ====== ");
            XFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            System.out.println(" ====== WL-Proxy-Client-IP ====== ");
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            System.out.println(" ====== HTTP_CLIENT_IP ====== ");
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            System.out.println(" ====== HTTP_X_FORWARDED_FOR ====== ");
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            System.out.println(" ====== getRemoteAddr ====== ");
            XFor = request.getRemoteAddr();
        }
        return XFor;
    }

    @RequestMapping(value="/thirdPartyOauth",method = RequestMethod.GET)
    public String getAuthorizationOauth(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException, UnsupportedEncodingException {
        logger.info(" ====== /thirdPartyOauth ====== ");
        String clientId = request.getParameter("clientid");
        logger.info(" ====== clientId ====== "+clientId);
        String redirectUrl = request.getParameter("redirect_uri");
        String data = request.getParameter("data");
        String state = request.getParameter("state");
        logger.info(" ====== state ====== "+state);
        String platform = "";
        if(redirectUrl!=null&&!redirectUrl.equals("")){//天猫
            redirectUrl = request.getParameter("redirect_uri");
            platform = "tmall";
            logger.info(" ====== "+platform+" ====== redirectUrl ====== "+redirectUrl);
        }
        if(data!=null&&!data.equals("")){//rokid
            redirectUrl = request.getParameter("data");
            platform = "rokid";
            logger.info(" ====== "+platform+"  ====== redirectUrl ====== "+redirectUrl);
        }

        redirectUrl = URLDecoder.decode(redirectUrl,"UTF-8");

        OAuthClientRequest oAuthClientRequest =
                OAuthClientRequest
                        .authorizationLocation("http://localhost:8815/authorization/getOauthCode")
                        .setClientId(clientId)
                        .setRedirectURI("http://localhost:8815/authorization/thirdPartyGetToken")
                        .setResponseType("code")
                        .buildBodyMessage();

        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse codeResponse = oAuthClient.resource(
                oAuthClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        String code = codeResponse.getBody().replaceAll("\"","");
        logger.info("code ====== "+code);
        String ip = request.getHeader("Host");
        if(platform.equals("tmall")){
            redirectUrl = redirectUrl+"&code="+code+"&state="+state;
            logger.info("return "+platform+"  ====== "+redirectUrl);
            return redirectUrl;
        }else{
            String url = "https://"+ip+
                   "/authorization/thirdPartyOauth?responsetype=code&clientid="+clientId+
                   "&state="+state+"&redirect_uri="+data;
            logger.info("return "+platform+" ====== "+url);
            return url;
        }
    }

    @RequestMapping(value ="getOauthCode",method = RequestMethod.GET)
    public String getAuthorizationCode(HttpServletRequest httpServletRequest,
                                     HttpServletResponse response) throws OAuthProblemException, OAuthSystemException {
            OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            String authCode = oAuthIssuer.authorizationCode();
            return authCode;
    }

    @RequestMapping(value ="/thirdPartyGetToken",method = RequestMethod.GET)
    public Object getToken(HttpServletRequest httpServletRequest,
                               HttpServletResponse response) throws OAuthProblemException, OAuthSystemException {
        String clientId = httpServletRequest.getParameter("clientid");
        String code = httpServletRequest.getParameter(OAuth.OAUTH_CODE);
        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation("http://localhost:8815/authorization/oauthCreateToken")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(clientId)
                .setClientSecret(UUID.randomUUID().toString())
                .setRedirectURI("http://localhost:8815/authorization/thirdPartyOauth")
                .setCode(code)
                .buildQueryMessage();
        request.addHeader("Accept", "application/json");
        request.addHeader("Content-Type", "application/json");
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthAccessTokenResponse tokenResponse =
                oAuthClient.accessToken(request, OAuth.HttpMethod.POST);

        tokenResponse.getRefreshToken();

        OAuthToken oAuthToken = tokenResponse.getOAuthToken();
        logger.info(" ====== getOAuthToken = AccessToken ------ "
                + oAuthToken.getAccessToken()+" RefreshToken ------ "
                + oAuthToken.getRefreshToken()+" ExpiresIn ------ "
                + oAuthToken.getExpiresIn());
        return tokenResponse.getOAuthToken();
    }

    @RequestMapping(value="oauthCreateToken",method = RequestMethod.POST)
    public Object createToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException , OAuthSystemException {
            String accessToken = UUID.randomUUID().toString();
            String refreshToken = UUID.randomUUID().toString();
            logger.info(" ====== "+accessToken);
            logger.info(" ====== "+refreshToken);
//            OAuthResponse response = OAuthASResponse
//                    .tokenResponse(HttpServletResponse.SC_OK)
//                    .setAccessToken(accessToken)
//                    .setExpiresIn("86400")
//                    .setRefreshToken(refreshToken)
//                    .buildBodyMessage();

//            httpServletResponse.setStatus(response.getResponseStatus());
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("access_token",accessToken);
            map.put("refresh_token",refreshToken);
            map.put("expires_in",86400);
            map.put("status",HttpServletResponse.SC_OK);
            return map;
    }
}
