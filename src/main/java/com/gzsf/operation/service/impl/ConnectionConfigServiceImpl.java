package com.gzsf.operation.service.impl;

import com.gzsf.operation.model.ConnectionConfig;
import com.gzsf.operation.dao.ConnectionConfigDao;
import com.gzsf.operation.service.ConnectionConfigService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;


@Service("connectionConfigService")
public class ConnectionConfigServiceImpl implements ConnectionConfigService {
    @Autowired
    private ConnectionConfigDao connectionConfigDao;

    @Override
    public ConnectionConfig getById(Integer connectionId) {
        return this.connectionConfigDao.getById(connectionId);
    }


    @Override
    public ConnectionConfig update(ConnectionConfig connectionConfig) {
        this.connectionConfigDao.update(connectionConfig);
        return this.getById(connectionConfig.getConnectionId());
    }


}