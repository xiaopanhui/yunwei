package com.gzsf.operation.dao;


import com.github.pagehelper.Page;
import com.gzsf.operation.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int update(User user);
    User getUserById(Long userId);
    User login(@Param("userName")String userName,@Param("password")String password);
    int insert(User user);
    void delete(Long userId);
    //查询所有用户
//    List<User> findAll();
  Page<User> getUsersByRole(@Param("role") User.Role role, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);



}
