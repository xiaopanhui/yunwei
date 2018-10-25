package com.gzsf.operation.exception;

public class DbConnectException extends BaseException {
    public DbConnectException(){
        super(303,"Db Cannot Connect");
    }
    public DbConnectException(Throwable throwable){
        this();
        this.initCause(throwable);
    }

}