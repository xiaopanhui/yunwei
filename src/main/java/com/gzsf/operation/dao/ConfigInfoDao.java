package com.gzsf.operation.dao;

import com.gzsf.operation.model.ConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ConfigInfoDao {

    ConfigInfo getByConfigInfoId(@Param("configId")Integer configId);

    List<ConfigInfo> getAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<ConfigInfo> getAll(ConfigInfo configInfo);

   Integer insert(ConfigInfo configInfo);

    Integer update(ConfigInfo configInfo);

   Integer deleteByConfigInfoId(Integer configId);

}