package com.gzsf.operation.model;

import lombok.Data;

@Data
public class ProxyInfo {
    private Long proxyId;
    private String requestUrl;
    private String targetUrl;
    private String method;
    private String whiteList;
    private String name;
    private int concurrent;

    private Long lastUpdate;

}
