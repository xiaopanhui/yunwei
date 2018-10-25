package com.gzsf.operation.service.impl;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.ConfigInfoCache;
import com.gzsf.operation.exception.NameAlreadyExistException;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.dao.ConfigInfoDao;
import com.gzsf.operation.service.ConfigInfoService;
import com.gzsf.operation.service.DbConnectService;
import com.gzsf.operation.service.MonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Date;


@Service
public class ConfigInfoServiceImpl extends MonoService implements ConfigInfoService {
    @Autowired
    private ConfigInfoDao configInfoDao;
    @Autowired
    private ConfigInfoCache configInfoCache;
    @Autowired
    private DbConnectService dbConnectService;

    private final String countSQLTemple="select count(*) from %s";
    private final String selectSQLTemple="select %s from %s limit %d,%d";

    @Override
    public  Mono<ConfigInfo> getByConfigInfoId(Long configId) {
        return async(()->{
            ConfigInfo config = configInfoCache.getByConfigInfoId(configId);
            if(config==null){
                throw new NoUserFoundException();
            }
            return config;
        });
    }
    /**
     * 新增数据
     */
    @Override
    public Mono<ConfigInfo> insert(ConfigInfo configInfo) {
        return async(() -> {
            return  configInfoCache.getByConfigInfoId(configInfoCache.insert(configInfo));
        });

    }
    /*
     *修改数据
     */

    @Override
    public   Mono<ConfigInfo> update(Long id,ConfigInfo configInfo) {

        return async(()->{
            ConfigInfo  configInfo1= configInfoDao.getByConfigInfoId(id);
            if(configInfo1==null){
                throw new NoUserFoundException();
            }
            configInfo.setUpdatedAt(new Date());
            configInfoCache.update(configInfo);
            return configInfo;

        });
    }

    @Override
    public Mono deleteById(Integer configId) {
        return  async(()->{
            configInfoCache.deleteByConfigInfoId(configId);
            return  "删除成功";
        });
    }

    @Override
    public Mono<ConfigInfo> getByName(String name) {
        return async(()->{
            return configInfoDao.getByName(name);
        });
    }
    //通过关键词查询,name,tableName,Dbid,ServiceId
    @Override
    public Mono<Page> getListConfig(ConfigInfo configInfo, Integer pageNum, Integer pagesize) {
        return  async(()->{
            return configInfoDao.getConfigs(configInfo.getName(),configInfo.getTableName(),
                    configInfo.getDbId(),configInfo.getServiceId(),pageNum,pagesize);
        });
    }

    public Mono<Page> getConfigList(int pageNum, int pagesize, String keyword) {
        return async(()->configInfoDao.getConfigList(pageNum,pagesize,keyword));
    }


}