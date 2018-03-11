package com.snh.weixin.api;

import com.snh.weixin.entity.UserAccessToken;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * 微信网站API
 */
public interface WxSiteApi {
    /**
     * 通过code换取用户access_token
     * 需要注意的是，由于安全方面的考虑，对访问该链接的客户端有IP白名单的要求。
     *
     * @param appid      公众号的appid
     * @param secret     应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     * @param code       填写第一步获取的code参数
     * @param grant_type 填authorization_code
     * @return UserAccessToken
     */
    @GET("/sns/oauth2/access_token")
    UserAccessToken getOAuthAccessToken(@Query("appid") String appid,
                                        @Query("secret") String secret,
                                        @Query("code") String code,
                                        @Query("grant_type") String grant_type);

    /**
     * 刷新access_token（如果需要）
     * 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，
     * refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。
     *
     * @param appid         公众号的appid
     * @param grant_type    填refresh_token
     * @param refresh_token 填写通过access_token获取到的refresh_token参数
     * @return UserAccessToken
     */
    @GET("/sns/oauth2/refresh_token")
    UserAccessToken refreshOAuthAccessToken(@Query("appid") String appid,
                                            @Query("grant_type") String grant_type,
                                            @Query("refresh_token") String refresh_token);

}
