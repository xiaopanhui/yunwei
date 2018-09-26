package com.gzsf.operation.security;

import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.model.User;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


public interface UserAuthRepository {
    /**
     * 通过token获取用户登陆信息
     * @param token token字符串
     * @return  验证信息
     */
    Authentication load(String token);

    /**
     * 延长过期时间
     * @param token token字符串
     * @param minutes 延长分钟数
     */
    void expire(String token, Long minutes);

    /**
     * 延长过期时间
     * @param token token字符串
     */
    void expire(String token);

    /**
     * 延长过期时间
     * @param token token字符串
     * @param time 延长时间
     * @param timeUnit 延长单位
     */
    void expire(String token, Long time, TimeUnit timeUnit);

    /**
     * 删除登陆信息
     * @param token 用户token
     */
    void delete(String token);

    /**
     * 删除用户所有活动登陆
     * @param userId 用户Id
     */
    void delete(Long userId);

    /**
     * 保存登陆信息
     * @param user 用户信息
     * @return token 字符串
     */
    String save(User user);

    /**
     * 清理过期用户
     * @param userId 用户Id
     */
    void clear(Long userId);
}
