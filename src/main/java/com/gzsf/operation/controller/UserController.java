package com.gzsf.operation.controller;

import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.bean.LoginBean;
import com.gzsf.operation.bean.Response;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthentication;
import com.gzsf.operation.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    private final Logger logger= LoggerFactory.getLogger(getClass());

    @PostMapping("login")
    public Mono<Response<User>> login(@RequestBody LoginBean bean){
        return userService
                .login(bean.getUserName(),bean.getPassword())
                .map(it->ResponseUtils.success(it))
                .doOnError(throwable -> logger.error("login",throwable))
                .onErrorReturn(ResponseUtils.noUser());

    }
    // 通过id,获取用户信息
    @GetMapping("/user/{id}")
    public  Mono<Response<User>> getUserById(@PathVariable("id") long id){
        return  userService.getUser(id)
                .map(it->ResponseUtils.success(it))
                .doOnError(throwable -> logger.error("getUserById",throwable))
                .onErrorReturn(ResponseUtils.noUser());
    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Mono getUsers(
            @RequestParam(value = "role",required = false) User.Role role,
            @RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            UserAuthentication userAuthentication){
            // TODO 把指定角色查出来
            if (role!=null&& role.ordinal() <= User.Role.ADMIN.ordinal()){
                return Mono.just(ResponseUtils.accessDenied());
            }
            return userService.getUserList(role, userName, pageNum,pageSize)
                    .map(it->ResponseUtils.successPage(it))
                    .doOnError(throwable -> logger.error("getUsers",throwable));
    }
    //更改用户信息
    @PatchMapping ("user")
    public Mono<Response<User>> changeUser(@RequestBody User body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (body.getRole()!=null &&body.getRole().ordinal()< User.Role.USER.ordinal()){
            body.setRole(null);
        }
        if (user.getRole()== User.Role.ADMIN || user.getUserId().equals(body.getUserId())){
            return userService.updateUser(body).map(it -> ResponseUtils.success(it))
                    .doOnError(throwable -> logger.error("changeUser", throwable));
        }
        return Mono.just(ResponseUtils.accessDenied());

    }
    //添加用户信息
    @PostMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public  Mono<Response<User>> addUser(@RequestBody User user){
             return userService.addUser(user).map(it->ResponseUtils.success(it))
//                     .onErrorReturn(ResponseUtils.UsersAlreadyExistException())
                     .doOnError(throwable -> logger.error("addUser",throwable));

    }

    @RequestMapping("auth")
    public Mono<Response> auth(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return Mono.just(ResponseUtils.success(user));
    }
    @RequestMapping("test")
    public Mono<Response> test(Authentication authentication) throws Exception {
//      throw new Exception("ddddd");
        //这个方法能得到user所有信息
      User user= (User) authentication.getPrincipal();
        return Mono.just(ResponseUtils.success(user));
    }
    @DeleteMapping("/user/{userId}")
    public  Mono deleteUser(@PathVariable("userId") long userId ){
        return userService.deleteUser(userId).map((it -> ResponseUtils.success(it)))
                .doOnError(throwable -> logger.error("deleteUser", throwable));

    }
}
