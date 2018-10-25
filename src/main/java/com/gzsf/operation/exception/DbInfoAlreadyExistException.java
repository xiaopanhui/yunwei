package com.gzsf.operation.exception;

public class DbInfoAlreadyExistException extends BaseException {
    public DbInfoAlreadyExistException(){
        super(302,"Db Already Exist");
    }
    public DbInfoAlreadyExistException(Throwable throwable){
        this();
        this.initCause(throwable);
    }

}