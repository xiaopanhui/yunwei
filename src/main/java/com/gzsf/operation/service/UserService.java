package com.gzsf.operation.service;

import com.gzsf.operation.Utils;
import com.gzsf.operation.bean.LoginBean;
import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService extends MonoService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthRepository userAuthRepository;

    public Mono<String> login(String userName, String password){
        final String pwd= Utils.SHA1(password);
        return async(()->{
            User user= userMapper.login(userName,pwd);
            if (user==null){
               throw new NoUserFoundException();
            }
            return userAuthRepository.save(user);
        });
    }
}
