package com.gzsf.operation.dao;

import com.github.pagehelper.Page;
import com.gzsf.operation.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)

public class UserMapperTest {
    @Autowired
   private UserMapper userMapper;

    @Test
    public void getUsersByRole() {
        Page<User> data = userMapper.getUsers(User.Role.READONLY, null, 1, 10);
        System.out.println(data);
    }
}