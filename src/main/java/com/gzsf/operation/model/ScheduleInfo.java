package com.gzsf.operation.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ScheduleInfo implements Serializable {
    private static final long serialVersionUID = 1138187169361170013L;
    private Long scheduleId;
    private String scheduleName;
    private TimerType timerType;
    private String timerValue;
    private TaskType taskType;
    private String taskValue;
    private String description;
    private Long createdBy;
    private Date createdAt;
    private Date updatedAt;
    private String extra;


    public enum TimerType{
        CRON
    }
    public  enum  TaskType{
        SQL,SHELL
    }
}
