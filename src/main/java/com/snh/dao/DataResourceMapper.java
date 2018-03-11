package com.snh.dao;

import com.snh.entity.DataResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataResourceMapper {
    int deleteByPrimaryKey(Long rId);

    int insert(DataResource record);

    int insertSelective(DataResource record);

    DataResource selectByPrimaryKey(Long rId);

    int updateByPrimaryKeySelective(DataResource record);

    int updateByPrimaryKey(DataResource record);

    List<DataResource> selectVideoList(@Param(value = "rname") String rname,@Param(value = "owner") String owner);

    List<DataResource> selectDocList();

    List<DataResource> selectPicList(@Param(value = "rname") String rname,@Param(value = "owner") String owner);

    int deleteResourceByPrimaryKey(long rId);

    DataResource selectMediaStatus(@Param(value = "objectKey") String objectKey);
}