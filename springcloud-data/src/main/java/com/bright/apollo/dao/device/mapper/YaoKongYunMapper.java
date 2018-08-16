package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TYaoKongYunBrand;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface YaoKongYunMapper {

    @Select("select * from t_yaokonyun_brand where b_id = #{tId}")
    @Results(value = {
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "bId",column = "b_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "deviceType",column = "device_type"),
            @Result(property = "common",column = "common")
    })
    List<TYaoKongYunBrand> getYaoKongYunByTId(@Param("tId") Integer tId);

}
