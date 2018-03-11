package com.snh.dao;

import com.snh.entity.LogOperation;
import com.snh.entity.LogOperationParams;

import java.util.List;

public interface LogOperationMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(LogOperation record);

    int insertSelective(LogOperation record);

    LogOperation selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(LogOperation record);

    int updateByPrimaryKey(LogOperation record);

    List<LogOperationParams> selectLogOperationList();
}