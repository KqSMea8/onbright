package com.bright.apollo.service;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TYaoKongYunBrand;

import java.util.List;

public interface YaoKongYunService {

    List<TYaoKongYunBrand> getYaoKongYunByTId(Integer tId);

}
