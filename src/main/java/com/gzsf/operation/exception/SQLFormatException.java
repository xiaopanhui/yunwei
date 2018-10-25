package com.gzsf.operation.exception;

public class SQLFormatException extends BaseException{
    public SQLFormatException() {
        super(306 ,"SQL Format Error");
    }

    public SQLFormatException(Throwable e) {
        this();
        this.initCause(e);
    }
}
