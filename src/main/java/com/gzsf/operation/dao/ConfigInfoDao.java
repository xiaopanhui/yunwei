package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.ConfigInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ConfigInfoDao {

    ConfigInfo getByConfigInfoId(@Param("configId")Long configId);

    List<ConfigInfo> getAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<ConfigInfo> getAll(ConfigInfo configInfo);

    Integer insert(ConfigInfo configInfo);

    Integer update(ConfigInfo configInfo);

    Integer deleteByConfigInfoId(Integer configId);
    ConfigInfo getByName(String name);
    Page<ConfigInfo> getConfigs(@Param("name") String name,@Param("tableName") String tableName,
                                @Param("dbId") Long dbId,@Param("serviceId")Long serviceId,
                                int pageNum,int pagesize);
    Page<ConfigInfo> getConfigList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize,@Param("keyword")String keyword );
    String getFields(Long configId);
    int updateFields(@Param("fields") String fields,@Param("configId") Long configId);
    void clean();
}