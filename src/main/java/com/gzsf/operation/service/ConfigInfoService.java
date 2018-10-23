package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.ConfigInfo;
import com.gzsf.operation.model.LogItem;
import org.apache.ibatis.annotations.Param;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ConfigInfoService {

    Mono<ConfigInfo> getByConfigInfoId(Long configId);

    Mono<ConfigInfo> insert(ConfigInfo configInfo);

    Mono<ConfigInfo> update( Long id,ConfigInfo configInfo);
    Mono deleteById(Integer configId);
    Mono<ConfigInfo> getByName(String name);
    Mono<Page> getListConfig( ConfigInfo configInfo, Integer pageNum, Integer pagesize);
    Mono<Page> getConfigList(int pageNum, int pagesize, String keyword);
}