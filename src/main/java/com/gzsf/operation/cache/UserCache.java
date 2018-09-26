package com.gzsf.operation.cache;

import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserCache {
    @Autowired
    private UserMapper userMapper;

    @Cacheable(value = "user",key = "#{userId}")
    public User getUserById(Long userId){
        return userMapper.getUserById(userId);
    }
}
