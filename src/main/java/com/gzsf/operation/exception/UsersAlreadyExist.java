package com.gzsf.operation.exception;

public class UsersAlreadyExist extends Exception {

    public   UsersAlreadyExist(){
        super("UsersAlreadyExist");
    }
    public   UsersAlreadyExist(String msg){
        super(msg);
    }
}
