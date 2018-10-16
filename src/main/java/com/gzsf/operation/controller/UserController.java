package com.gzsf.operation.controller;

import com.github.pagehelper.Page;
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

import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    private final Logger logger= LoggerFactory.getLogger(getClass());

    @PostMapping("login")
    public Mono<Response<String>> login(@RequestBody LoginBean bean){
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
            @RequestParam(value = "size", defaultValue = "0") int size,
            @RequestParam(value = "page", defaultValue = "-1") int page,
            UserAuthentication userAuthentication){
            // TODO 把指定角色查出来
              User user=new User();
              user.setUserName(userName);
              user.setRole(role);
              if(role!=null){
                  if (role.ordinal() <= User.Role.ADMIN.ordinal()){
                      return Mono.just(ResponseUtils.accessDenied());
                  }
              }
        return userService.getUserList(user, page,size)
                .map(it->ResponseUtils.successPage(it))
                .doOnError(throwable -> logger.error("getUsers",throwable));
    }
    //更改用户信息
    @PatchMapping ("user")
    public Mono<Response<User>> changeUser(@RequestBody Map<String,Object> body, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

      Long userId = Long.valueOf( body.get("userId").toString());
        String newPassword = (String) body.get("password");
        User.Role role = User.Role.valueOf(body.get("role").toString());
        if(role!=null){

        }
        if ((user.getRole().ordinal() < role.ordinal() && user.getRole() == User.Role.ADMIN)
                || user.getUserId() == userId) {
            return userService.updateUser(userId, newPassword, role).map(it -> ResponseUtils.success(it))
                    .doOnError(throwable -> logger.error("changeUser", throwable));
        }else {
            return Mono.just(ResponseUtils.accessDenied());
        }
    }
    //添加用户信息
    @PostMapping("/user")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public  Mono<Response<User>> addUser(@RequestBody User user){
             return userService.addUser(user).map(it->ResponseUtils.success(it))
//                     .onErrorReturn(ResponseUtils.UsersAlreadyExist())
                     .doOnError(throwable -> logger.error("addUser",throwable));

    }

    @RequestMapping("auth")
    public Mono<Response> auth(){
        return Mono.just(ResponseUtils.success(null));
    }
    @RequestMapping("test")
    public Mono<Response> test(Authentication authentication) throws Exception {
//      throw new Exception("ddddd");
        //这个方法能得到user所有信息
      User user= (User) authentication.getPrincipal();
        return Mono.just(ResponseUtils.success(user));
    }
}
