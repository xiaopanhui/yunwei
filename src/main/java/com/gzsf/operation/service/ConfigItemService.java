package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.Utils;
import com.gzsf.operation.cache.ConfigInfoCache;
import com.gzsf.operation.dao.ConfigInfoDao;
import com.gzsf.operation.exception.NoKeyFoundException;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.model.FieldItem;
import com.gzsf.operation.model.FieldItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ConfigItemService extends MonoService {
    private final String countSQLTemple="select count(*) from %s";
    private final String selectSQLTemple="select %s from %s limit %d,%d";
    private final String insertSQLTemple="insert into %s(%s) value( %s)";
    private final String updateSQLTemple="update %s set %s where %s";
    private final String deleteSQLTemple="delete from %s  where %s";

    @Autowired
    private ConfigInfoCache configInfoCache;
    @Autowired
    private DbConnectService dbConnectService;
    @Autowired
    private ConfigInfoDao configInfoDao;


    public Mono<List<FieldItem>> getConfigFields(Long configId){
        return async(()-> {
            String fiels= configInfoCache.getFields(configId);
            return Utils.StringToLogItems(fiels);
        });
    }

    public Mono<Boolean> updateConfigFields(String fields, Long configId){
        return async(()-> {
            configInfoCache.updateLogItems(fields,configId);
            return true;
        });
    }

    public Mono<Page> getConfig(Long id, int pageNum, int pageSize) {
        return async(()->getConfigPage(id, pageNum, pageSize));
    }

    private Page getConfigPage(Long id, int pageNum, int pageSize) throws Exception {
        Page page=new Page();
        String cofigs= configInfoCache.getFields(id);
        ConfigInfo configInfo = configInfoCache.getByConfigInfoId(id);
        FieldItems items = Utils.StringToLogItems(cofigs);
        StringBuffer stringBuffer=new StringBuffer();
        for (FieldItem item:items){
            stringBuffer.append("`").append(item.getKey()).append("`,");
        }
        if (stringBuffer.length()>0){
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        }
        String selectSQL= String.format(this.selectSQLTemple,stringBuffer.toString(),configInfo.getTableName(),(pageNum-1)*pageSize,pageSize);
        String countSQL= String.format(this.countSQLTemple,configInfo.getTableName());
        List list= dbConnectService.invokeSelect(selectSQL,configInfo.getDbId());
        page.addAll(list);
        int count=dbConnectService.invokeCount(countSQL,configInfo.getDbId());
        page.setTotal(count);
        page.setPages((count+pageNum-1)/pageSize);
        return page;
    }

    public Mono<Boolean> addCongItem(Long configId, Map<String,Object> body){
        return async(()->{
            ConfigInfo configInfo = configInfoCache.getByConfigInfoId(configId);
            String fieldStr= this.configInfoCache.getFields(configId);
            FieldItems fieldItems = Utils.StringToLogItems(fieldStr);
            StringBuffer keyBuffer=new StringBuffer();
            StringBuffer valueBuffer=new StringBuffer();
            for (FieldItem item:fieldItems){
                if (item.getType() == FieldItem.Type.KEY){
                    continue;
                }
                if (!body.containsKey(item.getKey())){
                    continue;
                }
                keyBuffer.append("`")
                        .append(item.getKey())
                        .append("`,");
                valueBuffer
                        .append("'")
                        .append(body.get(item.getKey()).toString().replace("'","\\'"))
                        .append("',");

            }
            keyBuffer.deleteCharAt(keyBuffer.length()-1);
            valueBuffer.deleteCharAt(valueBuffer.length()-1);
            String sql= String.format(this.insertSQLTemple,configInfo.getTableName(),keyBuffer,valueBuffer);
            return dbConnectService.invokeInsert(sql,configInfo.getDbId());
        });
    }

    public Mono<Integer> upateCongItem(Long configId, Map<String,Object> body){
        return async(()->{
            ConfigInfo configInfo = configInfoCache.getByConfigInfoId(configId);
            String fieldStr= this.configInfoCache.getFields(configId);
            FieldItems fieldItems = Utils.StringToLogItems(fieldStr);
            StringBuffer valueBuffer=new StringBuffer();
            String condition = null;
            for (FieldItem item:fieldItems){
                if (item.getType() == FieldItem.Type.KEY){
                    condition =item.getKey()+" = '"+body.getOrDefault(item.getKey(),"")+"'";
                    continue;
                }
                if (!body.containsKey(item.getKey())){
                    continue;
                }
                valueBuffer.append("`")
                        .append(item.getKey())
                        .append("` = '")
                        .append(body.get(item.getKey()))
                        .append("',");
            }
            if (condition==null){
                throw new NoKeyFoundException();
            }
            valueBuffer.deleteCharAt(valueBuffer.length()-1);
            String sql= String.format(this.updateSQLTemple,configInfo.getTableName(),valueBuffer,condition);
            return dbConnectService.invokeUpdate(sql,configInfo.getDbId());
        });
    }

    public Mono<Integer> deleteCongItem(Long configId, Map<String,Object> body){
        return async(()->{
            ConfigInfo configInfo = configInfoCache.getByConfigInfoId(configId);
            String fieldStr= this.configInfoCache.getFields(configId);
            FieldItems fieldItems = Utils.StringToLogItems(fieldStr);
            String condition = null;
            for (FieldItem item:fieldItems){
                if (item.getType() == FieldItem.Type.KEY ){
                    if (body.containsKey(item.getKey())){
                        condition =item.getKey()+" = '"+body.get(item.getKey())+"'";
                    }
                    break;
                }
            }
            if (condition==null){
                throw new NoKeyFoundException();
            }
            String sql= String.format(this.deleteSQLTemple,configInfo.getTableName(),condition);
            return dbConnectService.invokeUpdate(sql,configInfo.getDbId());
        });
    }
}
