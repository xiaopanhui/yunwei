package com.gzsf.operation.service;

import com.gzsf.operation.dao.*;
import com.gzsf.operation.model.FileVersionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CleanService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private FileVersionMapper fileVersionMapper;
    @Autowired
    private ConfigInfoDao configInfoDao;

    @Autowired
    private ScheduleInfoMapper scheduleInfoMapper;
    @PostConstruct
    @Scheduled(cron = "0 0 0 * * ?")
    public void clean(){
        userMapper.clean();
        userMapper.clean();
        serviceMapper.clean();
        logMapper.clean();
        fileMapper.clean();
        fileVersionMapper.clean();
        configInfoDao.clean();
        scheduleInfoMapper.clean();
    }
}
