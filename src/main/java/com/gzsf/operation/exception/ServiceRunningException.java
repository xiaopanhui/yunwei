package com.gzsf.operation.exception;


public class ServiceRunningException extends BaseException {
    public ServiceRunningException(){
        super(204,"Service is Running");
    }
}
