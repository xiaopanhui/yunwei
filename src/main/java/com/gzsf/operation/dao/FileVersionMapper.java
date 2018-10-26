package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.FileModel;
import com.gzsf.operation.model.FileVersionModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FileVersionMapper {
    Long insert( FileVersionModel fileModel);
    int delete(Long fileId);
    Page<FileVersionModel> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("fileId") Long fileId, @Param("keyword") String keyword);
    FileVersionModel getRecord(@Param("fileId")Long fileId,@Param("version") Integer version);
    Integer getLastVersion(Long fileId);
    void clean();

}
