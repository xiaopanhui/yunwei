package com.gzsf.operation.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.cache.UserCache;
import com.gzsf.operation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class UserAuthRepositoryRedis implements UserAuthRepository{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //ObjectMapper类是Jackson库的主要类。
    // 它提供一些功能将转换成Java对象匹配JSON结构，反之亦然。
    // 它使用JsonParser和JsonGenerator的实例实现JSON实际的读/写。

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserCache userCache;
    private final String tokenPrefix="token_";
    private final String userPrefix="token_user_";
    private ExecutorService executorService= Executors.newFixedThreadPool(12);
    @Override
    public Authentication load(String token) {
        String data= stringRedisTemplate.opsForValue().get(tokenPrefix+token);
        if (data==null||data.isEmpty())return new UserAuthentication();
        try {
            LoginInfo loginInfo= objectMapper.readValue(data, LoginInfo.class);
            executorService.submit(()->clear(loginInfo.getUserId()));
            loginInfo.setUser(userCache.getUserById(loginInfo.getUserId()));
            return new UserAuthentication(loginInfo);
        } catch (IOException e) {
            return new UserAuthentication();
        }
    }

    @Override
    public void expire(String token, Long minutes) {
        this.expire(token,minutes,TimeUnit.MINUTES);
    }

    @Override
    public void expire(String token) {
        this.expire(token,30L);
    }

    @Override
    public void expire(String token,Long time, TimeUnit timeUnit) {
        this.stringRedisTemplate.expire(tokenPrefix+token,time,timeUnit);
    }

    @Override
    public void delete(String token) {
        this.stringRedisTemplate.delete(tokenPrefix+token);
    }

    @Override
    public void delete(Long userId) {
        String data = stringRedisTemplate.opsForValue().get(userPrefix + userId);
        if (data==null)return;
        String tokens[] = data.split(",");
        for (String item : tokens) {
            stringRedisTemplate.delete(tokenPrefix + item);
        }
        stringRedisTemplate.delete(userPrefix + userId);
    }

    @Override
    public String save(User user) {
        LoginInfo info=new LoginInfo();
        info.setLoginAt(new Date());
        info.setUserId(user.getUserId());
        info.setUserName(user.getUserName());

        String token= UUID.randomUUID().toString();
        try {
            String data= objectMapper.writeValueAsString(info);
            stringRedisTemplate.opsForValue().set(tokenPrefix+token,data,30,TimeUnit.MINUTES);
            executorService.submit(()->addTokenForUser(token,user.getUserId()));
            return token;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private void addTokenForUser(String token,Long userId) {
        String data = stringRedisTemplate.opsForValue().get(userPrefix + userId);
        StringBuffer tokens = new StringBuffer(token);
        if (data != null) {
            tokens.append(',');
            tokens.append(data);
        }
        stringRedisTemplate.opsForValue().set(userPrefix + userId, tokens.toString());
    }

    @Override
    public void clear(Long userId) {
        String data = stringRedisTemplate.opsForValue().get(userPrefix + userId);
        StringBuffer newTokens = new StringBuffer();
        String tokens[] = data.split(",");
        for (int i = 0; i < tokens.length; i++) {
            if (stringRedisTemplate.hasKey(tokenPrefix + tokens[i])) {
                newTokens.append(tokens[i]);
                newTokens.append(',');
            }
        }
        if (newTokens.length()>0){
            newTokens.deleteCharAt(newTokens.length()-1);
        }
        stringRedisTemplate.opsForValue().set(userPrefix + userId, newTokens.toString());
    }
}
