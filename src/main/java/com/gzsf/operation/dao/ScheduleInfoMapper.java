package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.ScheduleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScheduleInfoMapper {
    ScheduleInfo getItemById(Long scheduleId);
    Page<ScheduleInfo> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keyword")String keyword);
    int update(ScheduleInfo scheduleInfo);
    Long insert(ScheduleInfo scheduleInfo);
    int delete(Long ScheduleId);
    int clean();
}
