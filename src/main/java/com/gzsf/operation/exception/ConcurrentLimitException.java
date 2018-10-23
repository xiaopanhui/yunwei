package com.gzsf.operation.exception;

public class ConcurrentLimitException extends BaseException {
    public ConcurrentLimitException() {
        super(602, "Concurrent Limit");
    }
}
