package com.gzsf.operation.service;

import ch.qos.logback.core.net.SyslogOutputStream;
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
/*

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
*/

    @Test
    public void updateUser() {
//        userService.updateUser((long) 1,"654321", User.Role.ADMIN).map(it->{
//
//            System.out.println("结果为"+it);
//            return "";
//        }).doOnError(new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }).block();
    }

    @Test
    public void addUser() {
        User user=new User();
        user.setRole(User.Role.ADMIN);
        user.setPassword("1");
        user.setUserName("zhi");
        userService.addUser(user).map(it->{
            System.out.println("结果为"+it);
            return "";
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }).block();

    }

    @Test
    public void selectByUserName() {
        userService.selectByUserName("xiao4").map(it->{
            System.out.println("结果为"+it);
            return "";
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
            }
        }).block();

    }

    @Test
    public  void StringFormatTest(){
        String str=null;
        str=String.format("Hi,%s", "王力");
        System.out.println("%s=StringFormat:"+str);
        str=String.format("Hi,%s:%s.%s", "王南","王力","王张");
        System.out.println(str);
        System.out.printf("100的一半是：%d %n", 100/2);
    }
}
