package com.bright.apollo.service.impl;


import com.bright.apollo.common.entity.TYaoKongYunBrand;
import com.bright.apollo.dao.device.mapper.YaoKongYunMapper;
import com.bright.apollo.service.YaoKongYunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YaoKongYunServiceImpl implements YaoKongYunService {

    @Autowired
    private YaoKongYunMapper yaoKongYunMapper;

    @Override
    public TYaoKongYunBrand getYaoKongYunByTId(Integer tId) {
        return yaoKongYunMapper.getYaoKongYunByTId(tId);
    }
}
