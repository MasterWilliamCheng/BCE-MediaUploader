package com.snh.weixin.entity;

/**
 * Redis键
 */
public enum RedisKey {
    USER_LOGIN("wx:user:login")//微信登录
    ;
    private String value;

    RedisKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
