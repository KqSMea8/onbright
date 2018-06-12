package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.OltuClientDetail;
import com.bright.apollo.dao.user.mapper.OltuMapper;
import com.bright.apollo.service.OltuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OltuServiceImpl implements OltuService {

    @Autowired
    private OltuMapper oltuMapper;

    @Override
    public OltuClientDetail getOltuCLentByClientIdAndclientSecret(String clientId, String clientSecret) {
        return oltuMapper.getOltuCLentByClientIdAndclientSecret(clientId,clientSecret);
    }
}
