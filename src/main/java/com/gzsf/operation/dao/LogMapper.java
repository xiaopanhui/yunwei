package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.LogModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogMapper {
    Long insert(LogModel model);
    int update(LogModel model);
    int delete(Long id);
    Page<LogModel> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keyword") String keyword);
    LogModel getRecordById(Long id);
    String getFields(Long logId);
    int updateFields(@Param("fields") String fields,@Param("logId") Long logId);
    void clean();
    void logdel(@Param("log_table")String log_table);
}
