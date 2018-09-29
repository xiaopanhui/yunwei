package com.gzsf.operation.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.LoginBean;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.bean.Response;
import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthentication;
import com.gzsf.operation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sun.rmi.runtime.Log;
import sun.text.normalizer.ICUData;

import java.util.List;
import java.util.function.Function;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private Page<User> startPage;

    @PostMapping("login")
    public Mono<Response<String>> login(@RequestBody LoginBean bean){
        return userService
                .login(bean.getUserName(),bean.getPassword())
                .map(it->ResponseUtils.success(it)).onErrorReturn(ResponseUtils.noUser());

    }
    // 通过id,获取用户信息
    @GetMapping("/user/{id}")
    public  Mono<Response<User>> getUserById(@PathVariable("id") long id){
        return  userService.getUser(id)
                .map(it->ResponseUtils.success(it)).onErrorReturn(ResponseUtils.noUser());
    }


/*
    public PageInfo<User> page(String username) {
//        startPage = PageHelper.<User>startPage(0, 8, "username asc");
//        List<User> userList = userMapper.findByUsernameLike(username);
//        PageInfo<User> pageInfo = PageInfo.of(userList);
//        return pageInfo;
    }
*/

    @GetMapping("/user")
    public Mono getUsers(
            @RequestParam(value = "size", defaultValue = "0") int size,
            @RequestParam(value = "page", defaultValue = "-1") int page,
            UserAuthentication userAuthentication){

        return userService.getUserList(((LoginInfo)userAuthentication.getDetails()).
                getUser().getRole(),page,size).map(it->ResponseUtils.success(it));
    }

    @RequestMapping("test")
    public Mono<Response> test(Authentication authentication) throws Exception {
//        throw new Exception("ddddd");
        //nneg得到user所有xx
        User user= (User) authentication.getPrincipal();
        return Mono.just(ResponseUtils.success(user));
    }
}
