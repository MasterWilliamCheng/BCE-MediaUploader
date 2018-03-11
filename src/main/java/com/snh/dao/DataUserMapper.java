package com.snh.dao;

import com.snh.entity.DataUser;

public interface DataUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(DataUser record);

    int insertSelective(DataUser record);

    DataUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(DataUser record);

    int updateByPrimaryKey(DataUser record);

    DataUser selectByOpenid(String openid);
}