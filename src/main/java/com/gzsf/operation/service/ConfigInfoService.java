package com.gzsf.operation.service;

import com.gzsf.operation.model.ConfigInfo;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ConfigInfoService {

    Mono<ConfigInfo> getByConfigInfoId(Integer configId);

    Mono<ConfigInfo> insert(ConfigInfo configInfo);

    Mono<ConfigInfo> update( Integer id,ConfigInfo configInfo);
//
//    boolean deleteById(Integer configId);

}