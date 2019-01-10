package com.gzsf.operation.bean;

import lombok.Data;

@Data
public class Db_table {
    private int dbId;
    private String log_table;

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getLog_table() {
        return log_table;
    }

    public void setLog_table(String log_table) {
        this.log_table = log_table;
    }
}
