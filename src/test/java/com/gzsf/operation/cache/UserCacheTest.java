package com.gzsf.operation.cache;

import com.gzsf.operation.Utils;
import com.gzsf.operation.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserCacheTest {
    @Autowired
    private UserCache userCache;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getUserById() {
        User user= userCache.getUserById(1L);
        System.out.print(user);
    }

    @Test
    public void save() {
        User user=new User();
        user.setUserId(3L);
        user.setRole(User.Role.SYSTEM);
        user.setUserName("temp");
        user.setPassword(Utils.SHA1("temp"));
        userCache.save(user);
    }

    @Test
    public void deleteUser() {
        userCache.deleteUser(3L);
    }
}