package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.cache.DbInfoCache;
import com.gzsf.operation.dao.DbInfoMapper;
import com.gzsf.operation.model.DbInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DbInfoService extends MonoService {
    @Autowired
    private DbInfoMapper dbInfoMapper;
    @Autowired
    private DbInfoCache dbInfoCache;

    public Mono<Page> getDbIfoList(Integer limit, Integer offset, String keyword) {
        return async(()-> dbInfoMapper.getList(offset,limit,keyword));
    }

    public Mono<DbInfo> saveDbInfo(DbInfo dbInfo) {
        dbInfo.setCreatedBy(null);
        return async(()->dbInfoCache.save(dbInfo)).map(dbInfoCache::getDbInfoById);
    }

    public Mono<Boolean> delete(Long dbId) {
        return async(()->dbInfoCache.delete(dbId));
    }

}
