package com.gzsf.operation.exception;

public class DbQueryException extends BaseException {
    public DbQueryException(){
        super(304,"Db Query Error");
    }
    public DbQueryException(Throwable throwable){
        this();
        this.initCause(throwable);
    }

}