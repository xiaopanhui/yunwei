package com.gzsf.operation.service;

import com.gzsf.operation.bean.LogFile;
import com.gzsf.operation.bean.LogMessage;
import com.gzsf.operation.cache.LogCache;
import com.gzsf.operation.model.LogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LogFileService {
    @Autowired
    private ApplicationEventPublisher publisher;
//    private SimpMessagingTemplate template;
    private final Map<Long, LogFile> logFileMap;
    @Autowired
    private LogCache logCache;
    private final ExecutorService service= Executors.newSingleThreadExecutor();
    private byte[] bytes=new byte[1024];
    public LogFileService() {
        this.logFileMap=new HashMap<>();
    }

    public void addLogFile(Long id){
        LogModel model= logCache.getRecord(id);
        if (model==null)return;
        if (logFileMap.containsKey(id)){
            service.submit(()->logFileMap.get(id).update(model.getLogPath()));
        }else {
            LogFile file=new LogFile(id,model.getLogPath());
            logFileMap.put(id,file);
        }
        if (logFileMap.size()==1){
            this.service.submit(this::loop);
        }
    }

    public void  remove(Long id){
        if (logFileMap.containsKey(id)){
            LogFile logFile= logFileMap.get(id);
            logFile.close();
        }
        logFileMap.remove(id);
    }

    private void loop(){
        int count=0;
        for (HashMap.Entry<Long,LogFile> entry:logFileMap.entrySet()){
            LogFile logFile= entry.getValue();
            if (logFile.hasData()){
                count++;
                this.service.submit(()->this.sendMassage(logFile));
            }
        }
        if (count ==0){
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {

            }
        }
        if (logFileMap.size()==0){
            return;
        }
        this.service.submit(this::loop);

    }
    private void sendMassage(LogFile logFile){
       int size= logFile.read();
       if (size>0){
           publisher.publishEvent(logFile.getMessage());
//           template.convertAndSend("/log",new LogMessage(logFile.getLogId(),logFile.getLogPath(),s));
       }
    }
}
