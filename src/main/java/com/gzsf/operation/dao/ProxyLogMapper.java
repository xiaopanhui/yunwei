package com.gzsf.operation.dao;

import com.gzsf.operation.model.ProxyLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProxyLogMapper {
    int insert(ProxyLog log);
}
