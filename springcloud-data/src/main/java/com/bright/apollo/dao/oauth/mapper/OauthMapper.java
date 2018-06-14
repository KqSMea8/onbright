package com.bright.apollo.dao.oauth.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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
}
