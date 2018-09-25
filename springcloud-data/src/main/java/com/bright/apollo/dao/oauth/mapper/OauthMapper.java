package com.bright.apollo.dao.oauth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.OauthClientDetails;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年6月12日
 * @Version:1.1.0
 */

@Mapper
@Component
public interface OauthMapper {

	@Insert("insert into oauth_client_details(client_id,\n" + "resource_ids,\n" + "client_secret,\n" + "scope,\n"
			+ "authorized_grant_types,\n" + "web_server_redirect_uri,\n" + "authorities,\n" + "access_token_validity,\n"
			+ "refresh_token_validity,\n" + "additional_information,\n" + "autoapprove) "
			+ "values(#{clientId},#{resourceIds},#{clientSecret},#{scope},#{authorizedGrantTypes}"
			+ ",#{webServerRedirectUri},#{authorities},#{accessTokenValidity},#{refreshTokenValidity},#{additionalInformation},#{autoapprove})")
	@Options(useGeneratedKeys = false, keyProperty = "client_id", keyColumn = "client_id")
	void addOauthClientDetails(OauthClientDetails oauthClientDetails);

	/**  
	 * @param grantType
	 * @return  
	 * @Description:  
	 */
	@Select("select * from oauth_client_details where authorized_grant_types like '%${grantType}%'")
	@Results(value = {
            @Result(column="client_id"   ,property="clientId"),
            @Result(column="resource_ids" ,  property="resourceIds"),
            @Result(column="client_secret" ,  property="clientSecret"),
            @Result(column="scope" ,  property="scope"),
            @Result(column="authorized_grant_types" ,  property="authorizedGrantTypes"),
            @Result(column="web_server_redirect_uri" ,  property="webServerRedirectUri"),
            @Result(column="authorities"  , property="authorities"),
            @Result(column="access_token_validity" ,  property="accessTokenValidity" ),
            @Result(column="refresh_token_validity"  , property="refreshTokenValidity" ),
            @Result(column="additional_information"  , property="additionalInformation"),
            @Result(column="autoapprove" ,  property="autoapprove")
    })
	List<OauthClientDetails> getClients(@Param("grantType")String grantType);

	/**  
	 * @param clientId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from oauth_client_details where client_id=#{clientId}")
	@Results(value = {
            @Result(column="client_id"   ,property="clientId"),
            @Result(column="resource_ids" ,  property="resourceIds"),
            @Result(column="client_secret" ,  property="clientSecret"),
            @Result(column="scope" ,  property="scope"),
            @Result(column="authorized_grant_types" ,  property="authorizedGrantTypes"),
            @Result(column="web_server_redirect_uri" ,  property="webServerRedirectUri"),
            @Result(column="authorities"  , property="authorities"),
            @Result(column="access_token_validity" ,  property="accessTokenValidity" ),
            @Result(column="refresh_token_validity"  , property="refreshTokenValidity" ),
            @Result(column="additional_information"  , property="additionalInformation"),
            @Result(column="autoapprove" ,  property="autoapprove")
    })
	OauthClientDetails queryClientByClientId(@Param("clientId")String clientId);
}
