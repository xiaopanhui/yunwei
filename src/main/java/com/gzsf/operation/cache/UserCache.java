package com.gzsf.operation.cache;

import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
/**
 * 带有缓存的 用户操作
 *
 * 数据dao层做修改,插入,
 * 会调用save清理缓存
 */
@Service
public class UserCache {
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户信息
     * @param userId 根据用户Id获取信息
     * @return
     */
    @Cacheable(value = "user",key = "#{userId}")
    public User getUserById(Long userId){
        return userMapper.getUserById(userId);
    }

    /**
     * 插入或者更新用户信息
     * @param user 用户信息
     * @return 变更行数
     *  @CacheEvict注解用来清理缓存
     */
    @CacheEvict(value = "user",key = "#{user.userId}")
    public int save(User user){
        user.setUpdatedAt(new Date());
        if (user.getUserId()==null){
            user.setCreatedAt(new Date());
            return userMapper.insert(user);
        }else {
           return userMapper.update(user);
        }
    }

    /**
     * 删除用户
     * @param userId 用户Id
     *
     *   @CacheEvict注解用来清理缓存
     */
    @CacheEvict(value = "user",key = "#{userId}")
    public void deleteUser(Long userId){
        userMapper.delete(userId);
    }
}
