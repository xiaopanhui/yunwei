package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.FileModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {
    Long insert(FileModel fileModel);
    int update(FileModel fileModel);
    int delete(Long fileId);
    Page<FileModel> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keyword")String keyword);
    FileModel getRecordById(Long fileId);
}
