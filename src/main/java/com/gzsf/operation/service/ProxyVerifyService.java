package com.gzsf.operation.service;

import com.gzsf.operation.Utils;
import com.gzsf.operation.model.ProxyInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProxyVerifyService {
    private Map<String, AtomicInteger> concurrentMap=new HashMap<>();
    private Map<String, Integer> concurrentTotalMap=new HashMap<>();
    private final String[] paramField ={"idCard"};


    public boolean verifyField(Map<String,Object> params){
        if (params==null)return false;
        for (String item:paramField){
            Object value = params.get(item);
            if (Utils.isEmpty(value))return false;
        }
        return true;
    }

    public boolean ipAllow(String whitelist,String ip){
        if (whitelist==null||ip==null)return false;
        String[] list= whitelist.split("[,; ]");
        for (String item:list){
            if ("0.0.0.0".equals(item)|| ip.equals(item))return true;
        }
        return false;
    }

    public boolean verifyConcurrent(ProxyInfo proxyInfo){
        boolean result=false;
        AtomicInteger concurrent;
        String requestUrl=proxyInfo.getRequestUrl();
        if (concurrentTotalMap.containsKey(requestUrl)){
            int total= concurrentTotalMap.get(requestUrl);
            concurrent= concurrentMap.get(requestUrl);
            concurrent.addAndGet(proxyInfo.getConcurrent()- total);
        }else {
            concurrent=new AtomicInteger(proxyInfo.getConcurrent());
            concurrentMap.put(requestUrl,concurrent);
            concurrentTotalMap.put(requestUrl,proxyInfo.getConcurrent());
        }
        int last=concurrent.addAndGet(-1);
        if (last>-1){
            result=true;
        }
        return result;
    }

    public void requestDone(String requestUrl){
        if (concurrentMap.containsKey(requestUrl)){
            AtomicInteger concurrent = concurrentMap.get(requestUrl);
            concurrent.addAndGet(1);
        }
    }

    @Scheduled(fixedDelay = 1000*60*60*30)
    private void clear(){
        for (String key:this.concurrentMap.keySet()){
            if (concurrentMap.get(key).get()>=concurrentTotalMap.get(key)){
                concurrentTotalMap.remove(key);
                concurrentMap.remove(key);
            }
        }
    }
}
