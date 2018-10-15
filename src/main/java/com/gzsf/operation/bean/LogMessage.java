package com.gzsf.operation.bean;

import lombok.Data;

import java.nio.ByteBuffer;

@Data
public class LogMessage {
    private Long logId;
    private String logPath;
    private ByteBuffer content;
    private int size;

    public LogMessage(Long logId, String logPath, ByteBuffer content) {
        this.logId = logId;
        this.logPath = logPath;
        this.content = content;
    }
}
