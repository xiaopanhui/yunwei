package com.gzsf.operation.cache;

import com.github.pagehelper.Page;
import com.gzsf.operation.dao.ScheduleInfoMapper;
import com.gzsf.operation.model.ScheduleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduleInfoCache {
    @Autowired
    private ScheduleInfoMapper scheduleInfoMapper;

    @CachePut(value = "schedule",key = "#scheduleInfo.scheduleId")
    public ScheduleInfo save(ScheduleInfo scheduleInfo){
        scheduleInfo.setUpdatedAt(new Date());
        if (scheduleInfo.getScheduleId() ==null){
            scheduleInfo.setCreatedAt(new Date());
            scheduleInfoMapper.insert(scheduleInfo);
        }else {
            scheduleInfoMapper.update(scheduleInfo);
        }
        return scheduleInfo;
    }


    @CacheEvict(value = "schedule",key = "#scheduleId")
    public int delete(Long scheduleId){
            return scheduleInfoMapper.delete(scheduleId);
    }

    @Cacheable(value = "schedule",key = "#scheduleId")
    public ScheduleInfo getitemById(Long scheduleId){
        return scheduleInfoMapper.getItemById(scheduleId);
    }
}
