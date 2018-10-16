
package com.gzsf.operation.dao;

import com.gzsf.operation.model.ConnectionConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface ConnectionConfigDao {

    ConnectionConfig getById(@Param("connectionId")Integer connectionId);

    int update(ConnectionConfig connectionConfig);
}