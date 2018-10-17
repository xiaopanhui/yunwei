package com.gzsf.operation.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.model.LogItem;
import com.gzsf.operation.model.LogItems;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_NULL_MAP_VALUES;

public class LogItemsTypeHandler extends BaseTypeHandler<LogItem[]> {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, LogItem[] items, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, this.toJson(items));
    }

    private String toJson(LogItem[] items) {
        try {
            return mapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }

    @Override
    public LogItem[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value= resultSet.getString(s);
        if (value==null)return null;
        try {
            return mapper.readValue(value,LogItem[].class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public LogItem[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return toObject(resultSet.getString(i));
    }

    @Override
    public LogItem[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return toObject(callableStatement.getString(i));
    }
    private LogItem[] toObject(String value) {

        try {
            return mapper.readValue(value, LogItem[].class);
        } catch (IOException e) {
            return null;
        }
    }

    static {
        mapper.configure(WRITE_NULL_MAP_VALUES, false);
        mapper.setSerializationInclusion(NON_NULL);
    }
}
