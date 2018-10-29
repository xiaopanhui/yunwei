package com.gzsf.operation.exception;


public class CronExpressionException extends BaseException {
    public CronExpressionException(){
        super(206,"Cron Expression Error");
    }

    public CronExpressionException(Throwable throwable) {
        this();
        initCause(throwable);
    }
}
