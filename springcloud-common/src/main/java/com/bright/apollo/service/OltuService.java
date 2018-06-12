package com.bright.apollo.service;

import com.bright.apollo.common.entity.OltuClientDetail;

public interface OltuService {

    public OltuClientDetail getOltuCLentByClientIdAndclientSecret(String clientId,String clientSecret);
}
