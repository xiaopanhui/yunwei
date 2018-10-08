package com.gzsf.operation.service;

import com.gzsf.operation.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}