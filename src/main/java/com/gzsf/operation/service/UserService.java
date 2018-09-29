package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.Utils;
import com.gzsf.operation.bean.LoginBean;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends MonoService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthRepository userAuthRepository;
//登录后信息做保存
    public Mono<String> login(String userName, String password){
        final String pwd= Utils.SHA1(password);
        return async(()->{
             User user = userMapper.login(userName,pwd);
            if (user==null){
               throw new NoUserFoundException();
            }
            return userAuthRepository.save(user);
        });
    }
    //通过用户获取id
    public  Mono getUser(long id){
        return  async(()->{
            User user=userMapper.getUserById(id);
            LoginInfo loginInfo=new LoginInfo();
            loginInfo.setRole(user.getRole());
            loginInfo.setUserName(user.getUserName());
            loginInfo.setLoginAt(new Date());
            loginInfo.setCreatedAt(user.getCreatedAt());
            loginInfo.setUpdatedAt(user.getUpdatedAt());
           return loginInfo;
        });
    }
    public Mono<Page> getUserList(User.Role role,int pageNum,int pageSize){
        return async(() -> {
            Page<User> users = userMapper.getUsersByRole(role, pageNum, pageSize);
            return users;
        });
    }





}
