package com.gzsf.operation.dao;


import com.github.pagehelper.Page;
import com.gzsf.operation.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    //修改用信息
    int update(User user);
    User getUserById(Long userId);
    User login(@Param("userName")String userName,@Param("password")String password);
    //返回数据类型更改
    int  insert(User user);
    void delete(Long userId);
    //查询用户
  Page<User> getUsers(@Param("role") User.Role role,@Param("userName")String userName,
                            @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

}
