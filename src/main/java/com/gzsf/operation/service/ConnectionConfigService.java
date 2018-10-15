package com.gzsf.operation.service;

import com.gzsf.operation.model.ConnectionConfig;
import java.util.List;


public interface ConnectionConfigService {

    ConnectionConfig getById(Integer connectionId);

    ConnectionConfig update(ConnectionConfig connectionConfig);


}