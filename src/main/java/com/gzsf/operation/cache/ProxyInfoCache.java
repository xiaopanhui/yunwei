package com.gzsf.operation.cache;

import com.gzsf.operation.dao.ProxyInfoMapper;
import com.gzsf.operation.model.ProxyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProxyInfoCache {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private Map<String, ProxyInfo> proxyInfoMap=new HashMap<>();
    @Autowired
    private ProxyInfoMapper proxyInfoMapper;
    private final long CACHE_TIME=1000*60;
    public ProxyInfo getProxyInfo(String requestUrl) {
        ProxyInfo proxyInfo = null;
        if (proxyInfoMap.containsKey(requestUrl)) {
            proxyInfo = proxyInfoMap.get(requestUrl);
            if (System.currentTimeMillis() - proxyInfo.getLastUpdate() < CACHE_TIME) {
                return proxyInfo;
            }
        }
        proxyInfo = proxyInfoMapper.getRecordByUrl(requestUrl);
        proxyInfo.setLastUpdate(System.currentTimeMillis());
        proxyInfoMap.put(requestUrl, proxyInfo);
        return proxyInfo;
    }
}
