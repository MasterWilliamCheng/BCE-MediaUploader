package com.snh.weixin.api;

import com.snh.weixin.data.ComponentTokenData;
import com.snh.weixin.entity.UserAccessToken;
import com.snh.weixin.entity.UserInfo;
import com.snh.weixin.entity.WeixinError;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;


/**
 * 微信第三方平台API
 */
public interface WxOpenApi {
    String COMPONENT_ACCESS_TOKEN = "component_access_token";

    /**
     * 获取第三方平台component_access_token
     * 第三方平台compoment_access_token是第三方平台的下文中接口的调用凭据，
     * 也叫做令牌（component_access_token）。每个令牌是存在有效期（2小时）
     * 的，且令牌的调用不是无限制的，请第三方平台做好令牌的管理，在令牌快过
     * 期时（比如1小时50分）再进行刷新。
     *
     * @param param 请求参数
     * @return ComponentTokenData
     */
//    @POST("/cgi-bin/component/api_component_token")
//    ComponentTokenData getApiComponentToken(@Body ComponentTokenParam param);

    /**
     * 该API用于获取预授权码。预授权码用于公众号授权时的第三方平台方安全验证。
     *
     * @param component_access_token 第三方平台component_access_token
     * @param param                  第三方平台方appid
     * @return PreAuthCode
     */
//    @POST("/cgi-bin/component/api_create_preauthcode")
//    PreAuthCode getApiCreatePreauthcode(@Query(COMPONENT_ACCESS_TOKEN) String component_access_token, @Body ComponentAppid param);

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     * 该API用于使用授权码换取授权公众号的授权信息，并换取authorizer_access_token
     * 和authorizer_refresh_token。 授权码的获取，需要在用户在第三方平台授权页中
     * 完成授权流程后，在回调URI中通过URL参数提供给第三方平台方。请注意，由于现在
     * 公众号可以自定义选择部分权限授权给第三方平台，因此第三方平台开发者需要通过
     * 该接口来获取公众号具体授权了哪些权限，而不是简单地认为自己声明的权限就是公
     * 众号授权的权限。
     *
     * @param component_access_token 第三方平台component_access_token
     * @param param                  参数
     * @return QueryAuthData
     */
//    @POST("/cgi-bin/component/api_query_auth")
//    QueryAuthData getQueryAuthData(@Query(COMPONENT_ACCESS_TOKEN) String component_access_token, @Body QueryAuthParam param);

    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）该API用于在授权方令牌（authorizer_access_token）
     * 失效时，可用刷新令牌（authorizer_refresh_token）获取新的令牌。请注意，此处token是2小时刷新
     * 一次，开发者需要自行进行token的缓存，避免token的获取次数达到每日的限定额度。缓存方法可以参考：
     * http://mp.weixin.qq.com/wiki/2/88b2bf1265a707c031e51f26ca5e6512.html
     *
     * @param component_access_token 第三方平台component_access_token
     * @param param                  参数
     * @return QueryAuthData
     */
//    @POST("/cgi-bin/component/api_authorizer_token")
//    AuthorizerToken getAuthorizerToken(@Query(COMPONENT_ACCESS_TOKEN) String component_access_token, @Body AuthorizerTokenParam param);

    /**
     * 获取授权方的公众号帐号基本信息
     * 该API用于获取授权方的公众号基本信息，包括头像、昵称、帐号类型、认证类型、微信号、原始ID和二维码图片URL。
     * 需要特别记录授权方的帐号类型，在消息及事件推送时，对于不具备客服接口的公众号，需要在5秒内立即响应；而若
     * 有客服接口，则可以选择暂时不响应，而选择后续通过客服接口来发送消息触达粉丝。
     *
     * @param component_access_token 第三方平台component_access_token
     * @param param                  参数
     * @return AuthorizerInfoData
     */
//    @POST("/cgi-bin/component/api_get_authorizer_info")
//    AuthorizerInfoData getAuthorizerInfo(@Query(COMPONENT_ACCESS_TOKEN) String component_access_token, @Body AuthorizerInfoParam param);

    /**
     * 获取授权方的选项设置信息
     * 该API用于获取授权方的公众号的选项设置信息，如：地理位置上报，语音识别开关，多客服开关。注意，获取各项选项
     * 设置信息，需要有授权方的授权，详见权限集说明。
     *
     * @param component_access_token 第三方平台component_access_token
     * @param param                  参数
     * @return AuthorizerOption
     */
//    @POST("/cgi-bin/component/api_get_authorizer_option")
//    AuthorizerOption getAuthorizerOption(@Query(COMPONENT_ACCESS_TOKEN) String component_access_token, @Body AuthorizerOptionParam param);

    /**
     * 设置授权方的选项信息
     * 该API用于设置授权方的公众号的选项信息，如：地理位置上报，语音识别开关，多客服开关。
     * 注意，设置各项选项设置信息，需要有授权方的授权，详见权限集说明。
     *
     * @param component_access_token 第三方平台component_access_token
     * @param param                  参数
     * @return WeixinError
     */
    @POST("/cgi-bin/component/api_set_authorizer_option")
//    WeixinError setAuthorizerOption(@Query(COMPONENT_ACCESS_TOKEN) String component_access_token, @Body SetAuthorizerOptionParam param);

    /**
     * 通过code换取用户access_token
     * 需要注意的是，由于安全方面的考虑，对访问该链接的客户端有IP白名单的要求。
     *
     * @param appid                  公众号的appid
     * @param code                   填写第一步获取的code参数
     * @param grant_type             填authorization_code
     * @param component_appid        服务开发方的appid
     * @param component_access_token 服务开发方的access_token
     * @return UserAccessToken
     */
    @GET("/sns/oauth2/component/access_token")
    UserAccessToken getOAuthAccessToken(@Query("appid") String appid,
                                        @Query("code") String code,
                                        @Query("grant_type") String grant_type,
                                        @Query("component_appid") String component_appid,
                                        @Query(COMPONENT_ACCESS_TOKEN) String component_access_token);

    /**
     * 刷新access_token（如果需要）
     * 由于access_token拥有较短的有效期，当access_token超时后，可以使用refresh_token进行刷新，
     * refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。
     *
     * @param appid                  公众号的appid
     * @param grant_type             填refresh_token
     * @param component_appid        服务开发方的appid
     * @param component_access_token 服务开发方的access_token
     * @return UserAccessToken
     */
    @GET("/sns/oauth2/component/refresh_token")
    UserAccessToken refreshOAuthAccessToken(@Query("appid") String appid,
                                            @Query("grant_type") String grant_type,
                                            @Query("refresh_token") String refresh_token,
                                            @Query("component_appid") String component_appid,
                                            @Query(COMPONENT_ACCESS_TOKEN) String component_access_token);

    /**
     * 通过网页授权access_token获取用户基本信息（需授权作用域为snsapi_userinfo）
     *
     * @param access_token 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openid       用户的唯一标识
     * @param lang         返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return UserInfo
     */
    @GET("/sns/userinfo")
    UserInfo getUserInfo(@Query("access_token") String access_token, @Query("openid") String openid, @Query("lang") String lang);


}
