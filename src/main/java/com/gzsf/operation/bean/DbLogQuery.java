package com.gzsf.operation.bean;

import lombok.Data;

@Data
public class DbLogQuery {
    private int offset;
    private int limit;
    private int keyword;
    private String startTime;
    private String endTime;
}
