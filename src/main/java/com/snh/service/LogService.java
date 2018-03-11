package com.snh.service;

import com.snh.entity.LogOperationParams;

import java.util.Date;
import java.util.List;

public interface LogService {
    //保存操作日志
    public int insertLogOperation(String resourceKey,Long userId,Date ctime,Integer type);
    //获取操作日志
    public List<LogOperationParams> getLogList();
}
