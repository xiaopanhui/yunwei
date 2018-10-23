package com.gzsf.operation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.dao.IdCardListMapper;
import com.gzsf.operation.dao.ProxyInfoMapper;
import com.gzsf.operation.dao.ProxyLogMapper;
import com.gzsf.operation.exception.*;
import com.gzsf.operation.model.IdCardList;
import com.gzsf.operation.model.ProxyInfo;
import com.gzsf.operation.model.ProxyLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClient;

import javax.swing.*;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProxyService extends MonoService{
    @Autowired
    private ProxyInfoMapper proxyInfoMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProxyLogMapper proxyLogMapper;
    @Autowired
    private IdCardListMapper idCardListMapper;
    @Autowired
    private ProxyVerifyService proxyVerifyService;

    private final ExecutorService logExecutor= Executors.newFixedThreadPool(12);

    public Mono executeProxy(String ip, String requestUrl, Map<String,Object> body) {
        final ProxyLog proxyLog = new ProxyLog();
        String paramStr = jsonToString(body);
        proxyLog.setUrl(requestUrl);
        proxyLog.setRequestParams(paramStr);
        proxyLog.setIp(ip);
        return async(() -> {
            if (!this.proxyVerifyService.verifyField(body)) {
                proxyLog.setStatus("参数不足");
                throw new ParamLackException();
            }
            ProxyInfo proxyInfo = proxyInfoMapper.getRecordByUrl(requestUrl);
            if (proxyInfo == null) {
                proxyLog.setStatus("地址不存在");
                throw new UrlNotFoundException();
            }
            if (!proxyVerifyService.ipAllow(proxyInfo.getWhiteList(), ip)) {
                proxyLog.setStatus("IP地址不允许");
                throw new IpIsNotAllowException();
            }
            int count = idCardListMapper.countByIdCard(body.get("idCard").toString());
            if (count > 0) {
                proxyLog.setStatus("限制请求");
                throw new QueryLimitException();
            }
            if (!proxyVerifyService.verifyConcurrent(proxyInfo)) {
                proxyLog.setStatus("并发限制");
                throw new ConcurrentLimitException();
            }
            return proxyInfo;
        })
                .flatMap(proxyInfo -> {
                    if ("POST".equals(proxyInfo.getMethod())) {
                        return post(proxyInfo.getTargetUrl(), body);
                    } else {
                        return get(proxyInfo.getTargetUrl(), body);
                    }
                }).doOnSuccess(map -> {
                    proxyLog.setResponseData(jsonToString(map));
                    proxyLog.setStatus("正常");
                    writeLog(proxyLog);
                }).doOnError(throwable -> {
                    if (!(throwable instanceof BaseException)) {
                        this.proxyVerifyService.requestDone(requestUrl);
                    }
                    writeLog(proxyLog);
                });
    }

    private void writeLog(ProxyLog log){
        logExecutor.submit(()->this.proxyLogMapper.insert(log));
    }

    private String jsonToString(Map json){
        try {
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    private Mono<Map> post(String url,Map body){
        return WebClient.create().post().uri(url).syncBody(body).retrieve().bodyToMono(Map.class);
    }

    private Mono<Map> get(String url,Map<String,Object> body){
        StringBuilder queryStr=new StringBuilder();
        for (Map.Entry<String,Object> entry: body.entrySet()){
            queryStr
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if (queryStr.length()>0){
            queryStr.deleteCharAt(queryStr.length()-1);
        }
        if (url.contains("?")){
            if ((!url.endsWith("?"))&&(!url.endsWith("&"))){
                queryStr.insert(0,"&");
            }
        }else {
            queryStr.insert(0,"?");
        }
         return WebClient.create().get().uri(url+queryStr.toString()).retrieve().bodyToMono(Map.class);
    }

}
