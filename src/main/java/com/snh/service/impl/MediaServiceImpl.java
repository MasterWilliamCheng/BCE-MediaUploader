package com.snh.service.impl;

import com.snh.dao.DataResourceMapper;
import com.snh.dao.LogOperationMapper;
import com.snh.entity.DataResource;
import com.snh.entity.LogOperation;
import com.snh.service.MediaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MediaServiceImpl implements MediaService{
    @Resource
    private DataResourceMapper dataResourceMapper;
    @Resource
    private LogOperationMapper logOperationMapper;

    @Transactional
    @Override
    public void insertResource(DataResource dataResource, LogOperation logOperation) {
        //新增资源文件
        int num = dataResourceMapper.insert(dataResource);
        //新增操作日志
        int num2 = logOperationMapper.insert(logOperation);
    }

    @Override
    public List<DataResource> getVideoList(String name,String owner) {
        List<DataResource> list = dataResourceMapper.selectVideoList(name,owner);
        return list;
    }

    @Override
    public List<DataResource> getDocList() {
        List<DataResource> list = dataResourceMapper.selectDocList();
        return list;
    }

    @Override
    public List<DataResource> getPicList(String name,String owner) {
        List<DataResource> list = dataResourceMapper.selectPicList(name,owner);
        return list;
    }

    @Transactional
    @Override
    public void deleteMedia(long rid,LogOperation logOperation) {
        int num = dataResourceMapper.deleteResourceByPrimaryKey(rid);
        int num2 = logOperationMapper.insert(logOperation);
    }

    @Transactional
    @Override
    public int updateMedia(DataResource dataResource) {
        int num = dataResourceMapper.updateByPrimaryKey(dataResource);
        return num;
    }

    @Override
    public int findMediaStatusByObjectKy(String objectKey) {
        DataResource dataResource = dataResourceMapper.selectMediaStatus(objectKey);
        int num = dataResource.getStatus();
        return num;
    }
}
