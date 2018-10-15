package com.gzsf.operation.exception;


public class NoUserFoundException extends BaseException{
    public NoUserFoundException() {
        super(405,"NoUserFound");
    }
}
