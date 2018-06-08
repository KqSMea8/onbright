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
    public void getAuthorizationOauth(HttpServletRequest request) throws OAuthSystemException,OAuthProblemException {
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
        System.out.println("getBody ------ "+codeResponse.getBody());
//        return "redirect:"+oAuthClientRequest.getLocationUri();
    }

    @RequestMapping(value ="getOauthCode",method = RequestMethod.GET)
    @ResponseBody
    public Object getAuthorizationCode(HttpServletRequest httpServletRequest,
                                     HttpServletResponse response) throws OAuthProblemException, OAuthSystemException {
            OAuthIssuerImpl oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
            String authCode = oAuthIssuer.authorizationCode();
            return new ResponseEntity(authCode,HttpStatus.OK);
    }

    @RequestMapping(value ="/thirdPartyGetToken",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getToken(HttpServletRequest httpServletRequest,
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

        //这里便拿到了 token, 可以做持久化.
        System.out.println("accessToken = " + accessToken);
        System.out.println("expiresIn = " + expiresIn);
        json.put("code",tokenResponse.getResponseCode());
        json.put("msg",tokenResponse.getBody());
        return json;
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
            Map<String,Object> json = new HashMap<String, Object>();
            json.put("access_token",accessToken);
            json.put("refresh_token",refreshToken);
            json.put("expires_in",3600);
            json.put("status",HttpServletResponse.SC_OK);
            System.out.println("------ "+json);
            return json;
    }
}
