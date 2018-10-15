package com.gzsf.operation.exception;

public class BaseException extends RuntimeException{
    private int error;

    public int getError() {
        return error;
    }

    public BaseException(int error,String message) {
        super(message);
        this.error=error;
    }
}
