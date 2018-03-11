package com.snh.service.impl;

import com.snh.dao.LogOperationMapper;
import com.snh.entity.LogOperation;
import com.snh.entity.LogOperationParams;
import com.snh.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Resource
    private LogOperationMapper logOperationMapper;

    @Override
    public int insertLogOperation(String resourceKey, Long userId, Date ctime, Integer type) {
        LogOperation logOperation = new LogOperation();
        logOperation.setResourceKey(resourceKey);
        logOperation.setUserId(userId);
        logOperation.setCtime(ctime);
        logOperation.setType(type);
        logOperation.setIsValid(true);
        int num = logOperationMapper.insert(logOperation);
        return num;
    }

    @Override
    public List<LogOperationParams> getLogList() {
        List<LogOperationParams> list = logOperationMapper.selectLogOperationList();
        return list;
    }
}
