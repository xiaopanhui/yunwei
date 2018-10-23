package com.gzsf.operation.exception;

public class UrlNotFoundException extends BaseException {
    public UrlNotFoundException() {
        super(600, "Url Not Found");
    }
}
