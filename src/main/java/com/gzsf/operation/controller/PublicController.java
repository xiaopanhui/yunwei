package com.gzsf.operation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.service.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    private ObjectMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ProxyService proxyService;
//    @PostMapping("{path}")
//    public Mono api(@RequestBody String data,HttpHeaders headers,@PathVariable("path") String path){
//        MediaType mediaType = headers.getContentType();
//        Map<String,Object> params;
//        if (mediaType.includes(MediaType.APPLICATION_JSON)){
//            params= parserJson(data);
//        }else if (mediaType.includes(MediaType.APPLICATION_FORM_URLENCODED)){
//            params =parserForm(data);
//        }
//        return Mono.just("Ok");
//    }

    @PostMapping("**")
    public Mono apiPost(@RequestBody String data,ServerHttpRequest req) {
        HttpHeaders headers = req.getHeaders();
        MediaType mediaType = headers.getContentType();
        Map<String, Object> params = null;
        if (mediaType.includes(MediaType.APPLICATION_JSON)) {
            params = parserJson(data);
        } else if (mediaType.includes(MediaType.APPLICATION_FORM_URLENCODED)) {
            params = parserForm(data);
        }
        return executeProxy(req,params);
    }

    @GetMapping("**")
    public Mono api(ServerHttpRequest req){
        MultiValueMap<String, String> valueMap= req.getQueryParams();
        Map<String,Object> params = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : valueMap.entrySet()){
            params.put(entry.getKey(),entry.getValue().get(0));
        }
        return executeProxy(req,params);
    }
    private Mono executeProxy(ServerHttpRequest req, Map<String,Object> params){
        String path= req.getPath().value().replace("/public/","");
        String ip = getIp(req);
        return proxyService.executeProxy(ip,path,params);
    }

    private Map<String,Object> parserJson(String data){
        try {
            return  mapper.readValue(data, Map.class);
        } catch (IOException e) {
            logger.error("parserJson",e);
        }
        return null;
    }

    private Map<String,Object> parserForm(String data){
        Map<String,Object> params=null;
        try {
            if (data==null)return null;
            data =  URLDecoder.decode(data,"UTF-8");
            params=new HashMap<>();
            String[] pairs= data.split("&");
            for (String pair : pairs){
                String[] kv= pair.split("=",2);
                if (kv.length!=2)continue;
                params.put(kv[0],kv[1]);
            }
        }catch (Exception e){
            logger.error("parserForm",e);
        }
        return params;
    }

    private String getIp(ServerHttpRequest request){
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip =headers.getFirst("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }
}
