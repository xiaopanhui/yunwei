package com.gzsf.operation.service;

import com.github.pagehelper.Page;
import com.gzsf.operation.ResponseUtils;
import com.gzsf.operation.Utils;
import com.gzsf.operation.bean.LoginBean;
import com.gzsf.operation.bean.LoginInfo;
import com.gzsf.operation.dao.UserMapper;
import com.gzsf.operation.exception.NoUserFoundException;
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

    /**
     *
     * @param role
     * @param userName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Mono<Page> getUserList(User.Role role,String userName ,int pageNum,int pageSize){
        return async(() -> {
            Page<User> users = userMapper.getUsers(role, userName, pageNum, pageSize);
            return users;
        });

    }
    //修改用户信息
    public  void  updateUser(Long id, String newPassword, User.Role role){


    }



    public void updateUser(User user) {
    /*    User newUser = new User();
        newUser.setUserId(user.getUserId());
        newUser.getRole(user.getRole());
        newUser.setPhone(phone);
        newUser.setEmail(email);
        // 根据id查询;返回user对象
        User user = userMapper.selectUserById(id);*/
      /*

       if (user == null) {
            // 如果user==null;抛出异常
            throw new UserNotFoundException("用户不存在");
        } else {
            // 根据用户名查询;返回user1
            User user1 = userMapper.selectUserByUsername(username);
            if (user1 != null) {

                // 当前的用户名就是登陆的用户名
                if (user1.getUsername().equals(user.getUsername())) {
                } else {
                    // 否则抛出异常
                    throw new UsernameAlreadyExistException("用户名已存在");
                }
            } else {
                // 数据库中没有相同的用户名,
                // 设置用户名为newUser的属性.
                newUser.setUsername(username);
            }
            // 修改用户信息
            userMapper.updateUser(newUser);

        }*/

    }




}
