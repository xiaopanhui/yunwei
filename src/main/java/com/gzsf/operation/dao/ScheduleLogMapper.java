package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.ScheduleInfo;
import com.gzsf.operation.model.ScheduleLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface ScheduleLogMapper {
//    ScheduleLog getItemById(Long scheduleId);
    Page<ScheduleLog> getList(@Param("scheduleId") Long scheduleId, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize,@Param("startTime") Date startTime,
                            @Param("endTime")  Date endTime);
    Long insert(ScheduleLog scheduleInfo);
}
