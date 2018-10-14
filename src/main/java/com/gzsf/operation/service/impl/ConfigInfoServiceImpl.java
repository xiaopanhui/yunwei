package com.gzsf.operation.service.impl;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.github.pagehelper.Page;
import com.gzsf.operation.cache.ConfigInfoCache;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.exception.UsersAlreadyExist;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.dao.ConfigInfoDao;
import com.gzsf.operation.service.ConfigInfoService;
import com.gzsf.operation.service.MonoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;


@Service
public class ConfigInfoServiceImpl extends MonoService implements ConfigInfoService {
    @Autowired
    private ConfigInfoDao configInfoDao;
    @Autowired
    private ConfigInfoCache configInfoCache;

    @Override
    public  Mono<ConfigInfo> getByConfigInfoId(Integer configId) {
        return async(()->{
            if( configInfoCache.getByConfigInfoId(configId)==null){
                throw new NoUserFoundException();}
                return   configInfoCache.getByConfigInfoId(configId);
        });
    }
    /**
     * 新增数据
     */
    @Override
    public Mono<ConfigInfo> insert(ConfigInfo configInfo) {
        return async(() -> {
            configInfo.setIsDel(false);
        return  configInfoCache.getByConfigInfoId(configInfoCache.insert(configInfo));
        });

    }
    /*
    *修改数据
    */

    @Override
    /*
    * 时间更改出错
    *
    **/
    public   Mono<ConfigInfo> update(Integer id,ConfigInfo configInfo) {

        return async(()->{
            ConfigInfo  configInfo1= configInfoDao.getByConfigInfoId(id);
            if(configInfo1==null){
                throw new NoUserFoundException();
            }

          if( configInfoDao.getByName(configInfo.getName())!=null){
              throw new UsersAlreadyExist();
          }
            configInfo1.setName(configInfo.getName());
            configInfo1.setTableName(configInfo.getTableName());
            configInfo1.setDbId(configInfo.getDbId());
            configInfo1.setServiceId(configInfo.getServiceId());
            configInfo1.setIsDel(false);
            configInfo1.setCreatedBy(configInfo.getCreatedBy());
            configInfo1.setUpdatedAt(new Date());
           configInfoDao.update(configInfo1);
           return configInfo1;

        });
    }

    @Override
    public Mono deleteById(Integer configId) {
        //大于表示删除成功
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
    @Override
    public Mono<Page> getListConfig(ConfigInfo configInfo, Integer pageNum, Integer pagesize) {
        return  async(()->{
            return configInfoDao.getConfigs(configInfo.getName(),configInfo.getTableName(),
                    configInfo.getDbId(),configInfo.getServiceId(),pageNum,pagesize);
        });
    }


}