package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.LoginBean;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.bean.Response;
import com.gzsf.operation.model.User;
import com.gzsf.operation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Mono<Response<String>> login(@RequestBody LoginBean bean){
        return userService
                .login(bean.getUserName(),bean.getPassword())
                .map(it->ResponseUtils.success(it)).onErrorReturn(ResponseUtils.noUser());

    }

    @RequestMapping("test")
    public Mono<Response> test(Authentication authentication) throws Exception {
//        throw new Exception("ddddd");
        User user= (User) authentication.getPrincipal();
        return Mono.just(ResponseUtils.success(user));
    }
}
