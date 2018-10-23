package com.gzsf.operation.dao;

import com.gzsf.operation.model.ProxyInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProxyInfoMapper {
    ProxyInfo getRecordByUrl(String requestUrl);
}
