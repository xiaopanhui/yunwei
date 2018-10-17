package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.DbInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DbInfoMapper {

    DbInfo getByDbInfoId(@Param("dbId")Long dbId);
    List<DbInfo> getAllByLimit(@Param("offset") int offset, @Param("limit") int limit);
    List<DbInfo> getAll(DbInfo dbInfo);
    Long insert(DbInfo dbInfo);
    Integer update(DbInfo dbInfo);
    Integer delete(Long id);
    DbInfo getByDbInfoName(String name);
    Page<DbInfo> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
    DbInfo getRecordById(Long dbId);
}
