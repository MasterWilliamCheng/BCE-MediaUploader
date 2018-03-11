package com.snh.weixin.data;

import com.snh.weixin.entity.WeixinError;

/**
 * 第三方平台component_access_token 返回内容
 *
 */
public class ComponentTokenData extends WeixinError {


    private String component_access_token;
    private int expires_in;

    public void setComponent_access_token(String component_access_token) {
        this.component_access_token = component_access_token;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getComponent_access_token() {
        return component_access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }
}
