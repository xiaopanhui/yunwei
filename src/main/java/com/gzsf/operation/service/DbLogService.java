package com.gzsf.operation.service;

import com.gzsf.operation.bean.DbLogQuery;
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
    protected String parseSQL(String sqlTemple, DbLogQuery params){
        VelocityContext context = new VelocityContext();
        context.put("start_time",params.getStartTime());
        context.put("end_time",params.getEndTime());
        context.put("keyword",params.getKeyword());
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context,stringWriter,"sqlTemple",sqlTemple);
        stringWriter.append(" limit");
//        stringWriter.append((params.getOffset()-1)*)
        return stringWriter.toString();
    }

    public List invoke(Long id, DbLogQuery params) throws Exception{
        LogModel logModel= logCache.getRecord(id);
        if (logModel==null)return null;
        String sql= parseSQL(logModel.getSql(),params);
        return dbConnectService.invoke(sql,logModel.getDbId());
    }
}
