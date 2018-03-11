package com.snh.weixin;

import com.snh.util.SpringPropertiesUtil;

/**
 * 微信配置
 */
public class WxConfig {
    //微信API服务器
    public static final String WEIXIN_API_SERVER = SpringPropertiesUtil.getProperty("WEIXIN_API_SERVER");

    //网站应用APPID
    public static final String WEIXIN_LOGIN_APP_ID=SpringPropertiesUtil.getProperty("WEIXIN_LOGIN_APP_ID");

    //网站应用APPSECRET
    public static final String WEIXIN_LOGIN_APP_SECRET=SpringPropertiesUtil.getProperty("WEIXIN_LOGIN_APP_SECRET");

    //微信第三方平台APP_ID（非公众号）
    public static final String COMPONENT_APP_ID = "";

    //微信审核通知模板ID
    public static final String WEIXIN_MOUDLE_NOTICE_ID=SpringPropertiesUtil.getProperty("WEIXIN_MOUDLE_NOTICE_ID");


    //测试用
    @Deprecated
    public static final String APP_ID = SpringPropertiesUtil.getProperty("APP_ID");
    @Deprecated
    public static final String APP_SECRET = SpringPropertiesUtil.getProperty("APP_SECRET");
    @Deprecated
    public static final String TOKEN = SpringPropertiesUtil.getProperty("TOKEN");



}
