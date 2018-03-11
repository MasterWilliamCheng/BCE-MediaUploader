package com.snh.service;


import com.snh.entity.DataUser;

/**
 * 用户服务
 */
public interface UserService {

    DataUser findByPrimaryId(Long userId);

    DataUser findUserByOpenid(String openid);

    void updateByOpenid(DataUser user);

    void insertUser(DataUser user);






}
