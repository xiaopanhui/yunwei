package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.DbInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbInfoMapper {
    Long insert(DbInfo dbInfo);
    int update(DbInfo dbInfo);
    int delete(Long dbId);
    DbInfo getDbInfoByDbInfoName(String dbInfoName);
    Page<DbInfo> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
    DbInfo getRecordById(Long dbId);
}
