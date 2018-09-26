package com.gzsf.operation.exception;

import java.util.concurrent.Executors;

public class NoUserFoundException extends Exception {
    public NoUserFoundException() {
        super("NoUserFound");
    }
}
