package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.DbInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DbInfoMapper {
    void clean();
    Long insert(DbInfo dbInfo);
    Integer update(DbInfo dbInfo);
    int delete(Long id);
    DbInfo getByDbInfoName(String name);
    DbInfo getByDbInfoId(@Param("dbId")Long dbId);
    Page<DbInfo> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
}
