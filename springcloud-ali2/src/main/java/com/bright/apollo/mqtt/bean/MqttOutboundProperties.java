package com.bright.apollo.mqtt.bean;

public class MqttOutboundProperties {
    private String url;
    private String userName;
    private String passWord;
    private String clientId;
    private String topics;

    public MqttOutboundProperties(){
//        System.out.println("MqttOutboundProperties  url "+url);
//        System.out.println("MqttOutboundProperties  userName "+userName);
//        System.out.println("MqttOutboundProperties  passWord "+passWord);
//        System.out.println("MqttOutboundProperties  clientId "+clientId);
//        System.out.println("MqttOutboundProperties  topics "+topics);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }
}
