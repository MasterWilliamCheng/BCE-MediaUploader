package com.snh.service;

import com.snh.entity.DataResource;
import com.snh.entity.LogOperation;

import java.util.List;

public interface MediaService {
    //保存上传资源信息
    public void insertResource(DataResource dataResource, LogOperation logOperation);
    //获取视频列表
    public List<DataResource> getVideoList(String name,String owner);
    //获取文档列表
    public List<DataResource> getDocList();
    //获取图片列表
    public List<DataResource> getPicList(String name,String owner);
    //删除资源文件
    public void deleteMedia(long rid,LogOperation logOperation);
    //更新资源文件
    public int updateMedia(DataResource dataResource);
    //查看资源文件状态
    public int findMediaStatusByObjectKy(String objectKey);
}
