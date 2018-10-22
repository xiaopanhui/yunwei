package com.gzsf.operation.service;

import com.gzsf.operation.cache.DbInfoCache;
import com.gzsf.operation.cache.LogCache;
import com.gzsf.operation.model.DbInfo;
import com.gzsf.operation.model.LogModel;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;

@Service
public class DbConnectService {
    private final Map<Long, DataSource> dataSourceMap=new HashMap<>();
    @Autowired
    private DbInfoCache dbInfoCache;

    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private DataSource createNewPool(Long id){
        DbInfo dbInfo = dbInfoCache.getByDbInfoId(id);
        if (dbInfo==null)return null;
        Properties properties =new Properties();
        properties.setProperty("driverClassName","com.mysql.jdbc.Driver");
        properties.setProperty("username",dbInfo.getUsername());
        properties.setProperty("password",dbInfo.getPassword());
        properties.setProperty("url",dbInfo.getUrl());
        properties.setProperty("testOnBorrow","true");
        properties.setProperty("validationQuery","select 1");
        properties.setProperty("initialSize","5");//初始化大小
        properties.setProperty("maxActive",dbInfo.getPoolSize().toString());//最大活动连接:连接池在同一时间能够分配的最大活动连接的数量, 如果设置为非正数则表示不限制
        properties.setProperty("minIdle","1");//最小空闲连接:连接池中容许保持空闲状态的最小连接数量,低于这个数量将创建新的连接,如果设置为0则不创建
        try {
            return BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            return null;
        }
    }

    public Connection getConnect(Long id) {
        try {
            if (id == null) return null;
            if (dataSourceMap.containsKey(id)) {

                return dataSourceMap.get(id).getConnection();

            } else {
                DataSource dataSource = createNewPool(id);
                if (dataSource == null) {
                    return null;
                }
                dataSourceMap.put(id, dataSource);
                return dataSource.getConnection();
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public List invoke(String sql,Long dbId) throws Exception{
        Connection connection = getConnect(dbId);
        if (connection==null)return null;
        PreparedStatement statement= null;
        List result=new ArrayList();
        try {
            statement= connection.prepareStatement(sql);
            ResultSet resultSet= statement.executeQuery();
            ResultSetMetaData metaData= resultSet.getMetaData();
            int size= metaData.getColumnCount();
            List<String> labels=new ArrayList<>();
            for (int i = 0; i < size; i++) {
                labels.add(metaData.getColumnLabel(i+1));
            }
            while (resultSet.next()){
                Map<String,Object> data=new HashMap<>();
                for (int i = 0; i < size; i++) {
                    data.put(labels.get(i),resultSet.getObject(i+1));
                }
                result.add(data);
            }
        }catch (Exception e){
            throw e;
        }finally {
            connection.close();
            if (statement!=null)statement.close();
        }
        logger.info("database query done");
        return result;

    }

    public Integer invokeCount(String sql,Long dbId) throws Exception{
        Connection connection = getConnect(dbId);
        if (connection==null)return null;
        PreparedStatement statement= null;
        int result = 0;
        try {
            statement= connection.prepareStatement(sql);
            ResultSet resultSet= statement.executeQuery();
          if ( resultSet.next()){
              result= resultSet.getInt(1);
          }
        }catch (Exception e){
            throw e;
        }finally {
            connection.close();
            if (statement!=null)statement.close();
        }
        logger.info("database query done");
        return result;

    }

}
