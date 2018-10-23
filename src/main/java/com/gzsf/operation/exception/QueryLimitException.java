package com.gzsf.operation.exception;

public class QueryLimitException extends BaseException {
    public QueryLimitException() {
        super(603, "Concurrent Limit");
    }
}
