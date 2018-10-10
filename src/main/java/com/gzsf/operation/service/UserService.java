package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.Utils;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.exception.UsersAlreadyExist;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

@Service
public class UserService extends MonoService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthRepository userAuthRepository;
    //登录后信息做保存
    public Mono<String> login(String userName, String password){
        final String pwd= Utils.SHA1(password);
        return async(()->{
             User user = userMapper.login(userName,pwd);
            if (user==null){
               throw new NoUserFoundException();
            }
            return userAuthRepository.save(user);
        });
    }
    //通过用户获取id
    public  Mono getUser(long id){
        return  async(()->{
            User user=userMapper.getUserById(id);
            LoginInfo loginInfo=new LoginInfo();
           loginInfo.setUser(user);
           return loginInfo;
        });
    }

    public Mono<Page> getUserList(User.Role role,String userName ,int pageNum,int pageSize){
        return async(() -> {
            Page<User> users = userMapper.getUsers(role, userName, pageNum, pageSize);
            return users;
        });

    }
    //修改用户信息
    public  Mono  updateUser(Long id, String newPassword, User.Role role){
        return  async(() ->{
            User user = userMapper.getUserById(id);
            if(user==null){
                throw new NoUserFoundException();
            }
            user.setPassword(Utils.SHA1(newPassword));
            user.setRole(role);
            userMapper.update(user);
            LoginInfo loginInfo=new LoginInfo();
            loginInfo.setUser(user);
            return loginInfo;
        });
    }
    //添加用户
   public Mono addUser(User user){
       return  async(() ->{
           User user1 = userMapper.getUserByUserName(user.getUserName());
            if (user1!=null){
                throw new UsersAlreadyExist("UsersAlreadyExist");
            }

           String password=Utils.SHA1(user.getPassword());
           user.setPassword(password);
           user.setUserName(user.getUserName());
           user.setCreatedAt(new Date());
           user.setRole(user.getRole());
           user.setPassword(user.getPassword());
           user.setUpdatedAt(new Date());
           userMapper.insert(user);
           return user;
       });
   }
    //通过用户获取id
    public  Mono  selectByUserName(String userName){
        return  async(()->{
            User user = userMapper.getUserByUserName(userName);
            return user;
        });
    }


}
