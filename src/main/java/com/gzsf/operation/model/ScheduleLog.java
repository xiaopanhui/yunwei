package com.gzsf.operation.model;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleLog {
    private Long logId;
    private Long scheduleId;
    private String scheduleName;
    private String execute;
    private String log;
    private boolean status;
    private Date createdAt;
}
