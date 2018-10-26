package com.gzsf.operation.exception;

public class UrlConnectException extends BaseException {
    public UrlConnectException() {
        super(605, "Url Connect Error");
    }

    public UrlConnectException(Throwable throwable) {
        this();
        initCause(throwable);
    }
}
