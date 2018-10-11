package com.gzsf.operation.exception;


public class ServiceNotRunningException extends BaseException {
    public ServiceNotRunningException(){
        super(203,"No Service Found");
    }
}
