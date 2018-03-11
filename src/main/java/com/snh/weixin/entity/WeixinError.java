package com.snh.weixin.entity;

/**
 * 错误信息
 * 所有Json返回数据都应该继承此类
 */
public class WeixinError {
    private int errcode;//错误码
    private String errmsg;//错误信息

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
