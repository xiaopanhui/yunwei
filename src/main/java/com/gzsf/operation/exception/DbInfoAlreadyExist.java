package com.gzsf.operation.exception;

public class DbInfoAlreadyExist extends RuntimeException {
    public   DbInfoAlreadyExist(){
        super("DbInfoAlreadyExist");
    }
    public   DbInfoAlreadyExist(String msg){
        super(msg);
    }
}