package com.gzsf.operation.model;

import lombok.Data;

@Data
public class ProxyLog {
    private Long id;
    private String name;
    private String url;
    private String ip;
    private String requestParams;
    private String responseData;
    private String status;
    private String createdAt;

}
