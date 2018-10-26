package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.bean.DbLogQuery;
import com.gzsf.operation.cache.LogCache;
import com.gzsf.operation.model.LogModel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.StringWriter;
import java.util.List;

@Service
public class DbLogService extends MonoService {
    @Autowired
    private LogCache logCache;
    @Autowired
    private DbConnectService dbConnectService;

    protected String parseQuerySQL(String sqlTemple, DbLogQuery params) {
        VelocityContext context = new VelocityContext();
        context.put("start_time", params.getStartTime());
        context.put("end_time", params.getEndTime());
        context.put("keyword", params.getKeyword());
        StringWriter stringWriter = new StringWriter();
        //速率的运行
        Velocity.evaluate(context, stringWriter, "sqlTemple", sqlTemple);
        stringWriter.append(" limit ");
        stringWriter.append(String.valueOf((params.getPageNum() - 1) * params.getPageSize()));
        stringWriter.append(" , ").append(String.valueOf(params.getPageSize()));
        return stringWriter.toString();
    }

    protected String parseCountSQL(String sqlTemple, DbLogQuery params) {
        VelocityContext context = new VelocityContext();
        context.put("start_time", params.getStartTime());
        context.put("end_time", params.getEndTime());
        context.put("keyword", params.getKeyword());
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, "sqlTemple", sqlTemple);
        return stringWriter.toString();
    }

    public Page invoke(Long id, DbLogQuery params) throws Exception {
        LogModel logModel = logCache.getRecord(id);
        if (logModel == null) return null;
        String sql = parseQuerySQL(logModel.getSql(), params);
        String countQql = parseCountSQL(logModel.getCountSql(), params);
        List list = dbConnectService.invoke(sql, logModel.getDbId());
        Page<Object> page = new Page<>();
        page.addAll(list);
        int count = dbConnectService.invokeCount(countQql, logModel.getDbId());
        page.setTotal(count);
        page.setPages((count + params.getPageSize() - 1) / params.getPageSize());
        return page;
    }

    public Mono<Page> getLogs(Long id, DbLogQuery params) {
        return async(() -> invoke(id, params));
    }
}
