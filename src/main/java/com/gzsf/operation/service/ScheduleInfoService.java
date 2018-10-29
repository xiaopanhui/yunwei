package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.DbInfoCache;
import com.gzsf.operation.cache.ScheduleInfoCache;
import com.gzsf.operation.dao.ScheduleInfoMapper;
import com.gzsf.operation.dao.ScheduleLogMapper;
import com.gzsf.operation.exception.BaseException;
import com.gzsf.operation.exception.CronExpressionException;
import com.gzsf.operation.model.DbInfo;
import com.gzsf.operation.model.ScheduleInfo;
import com.gzsf.operation.model.ScheduleLog;
import com.gzsf.task.CronTrigger;
import com.gzsf.task.TaskManager;
import com.gzsf.task.TaskRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

@Service
public class ScheduleInfoService extends MonoService {
    @Autowired
    private ScheduleInfoCache scheduleInfoCache;
    @Autowired
    private ScheduleInfoMapper scheduleInfoMapper;
    @Autowired
    private DbInfoCache dbInfoCache;
    @Autowired
    private DbConnectService dbConnectService;
    @Autowired
    private ScheduleLogMapper scheduleLogMapper;
    private TaskManager taskManager;
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private TaskRunnable<ScheduleInfo> taskRunnable=new TaskRunnable<ScheduleInfo>() {
        @Override
        public boolean run(ScheduleInfo data) throws Throwable {

            ScheduleLog scheduleLog=new ScheduleLog();
            scheduleLog.setScheduleId(data.getScheduleId());
            scheduleLog.setScheduleName(data.getScheduleName());
            scheduleLog.setExecute(data.getTaskValue());
            switch (data.getTaskType()){
                case SQL:{
                    Long dbId= Long.valueOf(data.getExtra());
                    dbConnectService.invoke(data.getTaskValue(),dbId);
                }
                case SHELL: {
                    String cmd= System.getProperty("os.name").toLowerCase().contains("windows")? "cmd /c " :"sh ";
                    Process process = Runtime.getRuntime().exec(cmd+ data.getTaskValue());
                    if (process.waitFor() != 0) {
                        scheduleLog.setStatus(false);
                    } else {
                        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        StringBuffer stringBuffer=new StringBuffer();
                        String line = "";
                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                            stringBuffer.append(line);
                        }
                        input.close();
                        scheduleLog.setLog(stringBuffer.toString());
                        scheduleLog.setStatus(true);
                    }
                }
            }
            scheduleLogMapper.insert(scheduleLog);
            return true;
        }

        @Override
        public void error(ScheduleInfo data,Throwable throwable){
            ScheduleLog scheduleLog=new ScheduleLog();
            scheduleLog.setScheduleId(data.getScheduleId());
            scheduleLog.setScheduleName(data.getScheduleName());
            scheduleLog.setExecute(data.getTaskValue());
            if (throwable instanceof BaseException){
                if (throwable.getCause()==null){
                    scheduleLog.setLog(throwable.getMessage());
                }else {
                    scheduleLog.setLog(throwable.getCause().getMessage());
                }
            }else {
                scheduleLog.setLog(throwable.getMessage());
            }
            scheduleLog.setStatus(true);
            logger.error("task",throwable);
        }
    };

    public ScheduleInfoService(){
        taskManager=new TaskManager(4);
        taskManager.start();
    }

    @PostConstruct
    public void  init(){
        new Thread(()->{
            int index=1;
            Page<ScheduleInfo> list=null;
            do {
                list = scheduleInfoMapper.getList(index++, 50, null);
                for (ScheduleInfo info:list){
                    try {
                        addTask(info);
                    }catch (Exception e){}
                }
            }while (list.size() == 20);
        }).start();
    }

    public Mono<ScheduleInfo> save(ScheduleInfo scheduleInfo) {
        return async(()->{
            taskManager.removeTask(scheduleInfo.getScheduleName());
            try {
                addTask(scheduleInfo);
            }catch (Exception e){
                throw  new CronExpressionException(e);
            }
          return   scheduleInfoCache.save(scheduleInfo);
        });
    }

    private void  addTask(ScheduleInfo info) {
        taskManager.addTask(info.getScheduleName(), info, new CronTrigger(info.getTimerValue()), taskRunnable);
    }

    public Mono<Boolean> delete(Long scheduleId) {
        return async(()->{
            ScheduleInfo scheduleInfo = scheduleInfoCache.getitemById(scheduleId);
            taskManager.removeTask(scheduleInfo.getScheduleName());
            return scheduleInfoCache.delete(scheduleId)==1;
        });
    }

    public Mono<ScheduleInfo> getitemById(Long scheduleId) {
        return async(()->scheduleInfoCache.getitemById(scheduleId));
    }

    public Mono<Page<ScheduleInfo>> getList(int pageNum, int pageSize, String keyword) {
        return async(()->scheduleInfoMapper.getList(pageNum, pageSize, keyword));
    }

    public Mono<Page<ScheduleLog>> getLogList(
            Long scheduleId ,int pageNum, int pageSize ,final Date startTime,
                                             final   Date endTime) {
        return async(()->scheduleLogMapper.getList(scheduleId, pageNum, pageSize, startTime, endTime));
    }
}
