package com.bright.apollo.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @RequestMapping(value="/thirdPartyOauth",method = RequestMethod.GET)
    public String getAuthorizationOauth(HttpServletRequest request) throws OAuthSystemException,OAuthProblemException {
        String clientId = request.getParameter("clientid");
        OAuthClientRequest oAuthClientRequest =
                OAuthClientRequest
                        .authorizationLocation("http://localhost:8815/authorization/getOauthCode")
                        .setClientId(clientId)
                        .setRedirectURI("http://localhost:8815/authorization/thirdPartyGetToken")
                        .setResponseType("code")
                        .buildBodyMessage();
//        System.out.println("location uri :"+oAuthClientRequest.getLocationUri());
//        HttpGet httpGet = new HttpGet(oAuthClientRequest.getLocationUri());
//        HttpClient client = HttpClientBuilder.create().build();
//        client.execute(httpGet);
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse codeResponse = oAuthClient.resource(
                oAuthClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        System.out.println("getResponseCode ------ "+codeResponse.getResponseCode());
        String code = codeResponse.getBody().replaceAll("\"","");
        System.out.println("getBody ------ "+code);
//        return "redirect:"+oAuthClientRequest.getLocationUri();
        System.out.println(request.getLocalPort());
        System.out.println(request.getLocalAddr());
        return "https://"+request.getLocalAddr()+":"+request.getLocalPort()+"/authorization/getOauthCode?client_id="+clientId+
                "&response_type=code&state=0&redirect_uri=https://"+request.getLocalAddr()+":"+request.getLocalPort()+
                "/authorization/thirdPartyGetToken?code="+code;
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
        JSONObject json = new JSONObject();
        String clientId = httpServletRequest.getParameter("clientid");
//        OAuthTokenRequest tokenRequest = new OAuthTokenRequest(httpServletRequest);
        String code = httpServletRequest.getParameter(OAuth.OAUTH_CODE);
        System.out.println("code ------ "+code);
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

        String accessToken = tokenResponse.getAccessToken();
        Long expiresIn = tokenResponse.getExpiresIn();
        tokenResponse.getRefreshToken();

        OAuthToken oAuthToken = tokenResponse.getOAuthToken();
        System.out.println("getOAuthToken = AccessToken ------ "
                + oAuthToken.getAccessToken()+" RefreshToken ------ "
                + oAuthToken.getRefreshToken()+" ExpiresIn ------ "
                + oAuthToken.getExpiresIn());
        return tokenResponse.getOAuthToken();
    }

    @RequestMapping(value="oauthCreateToken",method = RequestMethod.POST)
    public Object createToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException , OAuthSystemException {
            String accessToken = UUID.randomUUID().toString();
            String refreshToken = UUID.randomUUID().toString();
            System.out.println("accessToken ------ "+accessToken);
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn("3600")
                    .setRefreshToken(refreshToken)
                    .buildBodyMessage();

            httpServletResponse.setStatus(response.getResponseStatus());
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("access_token",accessToken);
            map.put("refresh_token",refreshToken);
            map.put("expires_in",3600);
            map.put("status",HttpServletResponse.SC_OK);
            return map;
    }
}
