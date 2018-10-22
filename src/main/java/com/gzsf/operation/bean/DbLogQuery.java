package com.gzsf.operation.bean;

import lombok.Data;

@Data
public class DbLogQuery {
    private int pageNum;
    private int pageSize;
    private String keyword;
    private String startTime;
    private String endTime;
}
