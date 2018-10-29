package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.ScheduleInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ScheduleInfoMapperTest {

    @Autowired
    private ScheduleInfoMapper scheduleInfoMapper;
    @Test
    public void getItemById() {
        ScheduleInfo scheduleInfo = scheduleInfoMapper.getItemById(1L);
        System.out.println(scheduleInfo);
    }

    @Test
    public void getList() {
       Page<ScheduleInfo> list= scheduleInfoMapper.getList(1,10,"0");
        System.out.println(list);
    }

    @Test
    public void update() {
    }

    @Test
    public void insert() {
        ScheduleInfo scheduleInfo=new ScheduleInfo();
        scheduleInfo.setCreatedAt(new Date());
        scheduleInfo.setCreatedBy(0L);
        scheduleInfo.setUpdatedAt(new Date());
        scheduleInfo.setTaskType(ScheduleInfo.TaskType.SHELL);
        scheduleInfo.setTaskValue("sh lll");
        scheduleInfo.setTimerType(ScheduleInfo.TimerType.CRON);
        scheduleInfo.setTimerValue("0 0 0 * *");
        scheduleInfo.setScheduleName("00o099");
        scheduleInfoMapper.insert(scheduleInfo);
    }

    @Test
    public void delete() {
         scheduleInfoMapper.delete(1L);
    }
}