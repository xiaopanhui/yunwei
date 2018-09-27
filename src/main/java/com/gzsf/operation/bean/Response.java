package com.gzsf.operation.bean;

import lombok.Data;

@Data
public class Response<T> {
    private int status;
    private String msg;
    private T data;

    public Response() {
    }

    public Response(int code, String msg) {
        this.status = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T data) {
        this.status = code;
        this.msg = msg;
        this.data = data;
    }
}
