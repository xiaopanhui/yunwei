package com.gzsf.operation.service;

import com.gzsf.operation.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void login() {
    }

    @Test
    public void getUser() {
    }

    @Test
    public void getUserList() {
        userService.getUserList(User.Role.ADMIN,""  ,1,10).map(it->{
            System.out.println(it);
            long total= it.getTotal();
            return "";
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }).block();
    }

    @Test
    public void updateUser() {
        userService.updateUser((long) 1,"654321", User.Role.ADMIN).map(it->{

            System.out.println("结果为"+it);
            return "";
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }).block();
    }
//
//    @Test
//    public void addUser() {
//        User user=new User();
//        user.setRole(User.Role.ADMIN);
//        user.setPassword("1");
//        user.setUserName("zhi");
//        user.setCreatedAt(new Date());
//        user.setUpdatedAt(new Date());
//        userService.addUser(user).map(it->{
//            System.out.println("结果为"+it);
//            return "";
//        }).doOnError(new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }).block();
//
//    }
}