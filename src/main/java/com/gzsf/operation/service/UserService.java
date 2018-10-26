package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.Utils;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.cache.UserCache;
import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.exception.NoUserFoundException;
import com.gzsf.operation.exception.UsersAlreadyExistException;
import com.gzsf.operation.model.User;
import com.gzsf.operation.security.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Date;

@Service
public class UserService extends MonoService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private UserCache userCache;
    //登录后信息做保存
    public Mono<User> login(String userName, String password){
        final String pwd= Utils.SHA1(password);
        return async(()->{
             User user = userMapper.login(userName,pwd);
            if (user==null){
               throw new NoUserFoundException();
            }
            user.setToken(userAuthRepository.save(user));
            return user;
        });
    }
    //通过用户获取id
    public  Mono getUser(long id){
        return  async(()->{
            User user=userMapper.getUserById(id);
            if(user==null){
               throw  new  NoUserFoundException();
            }
            LoginInfo loginInfo=new LoginInfo();
           loginInfo.setUser(user);
           return loginInfo;
        });
    }

/*    public Mono<Page> getUserList(User.Role role,String userName ,int pageNum,int pageSize){
        return async(() -> {
            Page<User> users = userMapper.getUsers(role, userName, pageNum, pageSize);
            return users;
        });

    }*/
    public Mono<Page> getUserList(User.Role role,String userName, int pageNum, int pageSize){
        return async(() -> {
            Page<User> users = userMapper.getUsers(role,userName, pageNum, pageSize);
            return users;
        });

    }


    //修改用户信息
    public  Mono  updateUser(User body){
        return  async(() ->{
            User user = userCache.getUserById(body.getUserId());
            if(user==null){
                throw new NoUserFoundException();
            }
            if (body.getPassword()!=null){
                userAuthRepository.delete(user.getUserId());
                user.setPassword(Utils.SHA1(body.getPassword()));
            }
            user.setDescription(body.getDescription());
            user.setRole(body.getRole());
            userCache.save(user);
            return user;
        });
    }
    //添加用户
   public Mono addUser(User user){
       return  async(() ->{
           User user1 = userMapper.getUserByUserName(user.getUserName());
            if (user1!=null){
                throw new UsersAlreadyExistException();
            }

           String password=Utils.SHA1(user.getPassword());

           user.setPassword(password);
           user.setCreatedAt(new Date());
           user.setUpdatedAt(new Date());
           userMapper.insert(user);
           //密码置空
           user.setPassword(null);
           return user;
       });
   }
    //通过用户获取id
    public  Mono  selectByUserName(String userName){
        return  async(()->{
            User user = userMapper.getUserByUserName(userName);
            LoginInfo loginInfo=new LoginInfo();
            loginInfo.setUser(user);
            return user;
        });
    }

     /*删除用户*/
    public Mono<Object> deleteUser(long id) {
        return async(() -> {
            userCache.deleteUser(id);
            userAuthRepository.delete(id);
            return true;
                }

        );
    }

}
