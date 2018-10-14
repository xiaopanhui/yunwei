package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.ConfigInfo;
import org.apache.ibatis.annotations.Param;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ConfigInfoService {

    Mono<ConfigInfo> getByConfigInfoId(Integer configId);

    Mono<ConfigInfo> insert(ConfigInfo configInfo);

    Mono<ConfigInfo> update( Integer id,ConfigInfo configInfo);
    Mono deleteById(Integer configId);
    Mono<ConfigInfo> getByName(String name);
    Mono<Page> getListConfig( ConfigInfo configInfo, Integer pageNum, Integer pagesize);



}