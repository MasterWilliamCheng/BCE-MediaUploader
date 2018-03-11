package com.snh.service.impl;

import com.snh.dao.DataUserMapper;
import com.snh.entity.DataUser;
import com.snh.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by dzp on 2017/12/14
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private DataUserMapper dataUserMapper;

    @Override
    public DataUser findByPrimaryId(Long userId) {
        return dataUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public DataUser findUserByOpenid(String openid) {
        return dataUserMapper.selectByOpenid(openid);
    }

    @Override
    public void updateByOpenid(DataUser user) {
        dataUserMapper.updateByPrimaryKeySelective(user);
    }

    @Transactional
    @Override
    public void insertUser(DataUser user) {
        dataUserMapper.insert(user);
    }


}
