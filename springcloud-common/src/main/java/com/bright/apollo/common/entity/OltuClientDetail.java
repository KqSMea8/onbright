package com.bright.apollo.common.entity;

public class OltuClientDetail {

    private String clientId;

    private String resourcesIds;

    private String clientSecret;

    private String scope;

    private String authorizedGrantType;

    private String webServerRedirectURI;

    private String authorities;

    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    private String addtionalInformation;

    private String autoApprove;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResourcesIds() {
        return resourcesIds;
    }

    public void setResourcesIds(String resourcesIds) {
        this.resourcesIds = resourcesIds;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthorizedGrantType() {
        return authorizedGrantType;
    }

    public void setAuthorizedGrantType(String authorizedGrantType) {
        this.authorizedGrantType = authorizedGrantType;
    }

    public String getWebServerRedirectURI() {
        return webServerRedirectURI;
    }

    public void setWebServerRedirectURI(String webServerRedirectURI) {
        this.webServerRedirectURI = webServerRedirectURI;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getAddtionalInformation() {
        return addtionalInformation;
    }

    public void setAddtionalInformation(String addtionalInformation) {
        this.addtionalInformation = addtionalInformation;
    }

    public String getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(String autoApprove) {
        this.autoApprove = autoApprove;
    }


}
