package com.gzsf.operation.exception;

public class UsersAlreadyExist extends RuntimeException {

    public   UsersAlreadyExist(){
        super("UsersAlreadyExist");
    }
    public   UsersAlreadyExist(String msg){
        super(msg);
    }
}
