package com.gzsf.operation.service;

import com.gzsf.operation.cache.LogCache;
import com.gzsf.operation.model.LogModel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class DbLogService {
    @Autowired
    private LogCache logCache;
    @Autowired
    private DbConnectService dbConnectService;
    protected String parseSQL(String sqlTemple, Map<String,Object> params){
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String,Object> item: params.entrySet()){
            context.put(item.getKey(),item.getValue());
        }
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context,stringWriter,"sqlTemple",sqlTemple);
        return stringWriter.toString();
    }

    public List invoke(Long id, Map<String,Object> params) throws Exception{
        LogModel logModel= logCache.getRecord(id);
        if (logModel==null)return null;
        String sql= parseSQL(logModel.getSql(),params);
        return dbConnectService.invoke(sql,logModel.getDbId());
    }
}
