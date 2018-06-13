package com.bright.apollo.dao.user.mapper;

import com.bright.apollo.common.entity.OltuClientDetail;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OltuMapper {

    @Select("select * from oauth_client_details where client_id = #{clientId} and client_secret = #{clientSecrect} ")
    @Results(value = {
            @Result(property = "clientId",column = "client_id"),
            @Result(property = "clientSecret",column = "client_secret")
    })
    OltuClientDetail getOltuCLentByClientIdAndclientSecret(@Param("clientId") String clientId, @Param("clientSecrect") String clientSecret);
}
