package com.bright.apollo.dao.user.mapper;

import com.bright.apollo.common.entity.OltuClientDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface OltuMapper {

    @Select("select * from oauth_client_details where client_id = #{clientId} and client_secret = #{clientSecrect} ")
    public OltuClientDetail getOltuCLentByClientIdAndclientSecret(@Param("clientId") String clientId, @Param("clientSecrect") String clientSecret);
}
