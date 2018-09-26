package com.gzsf.operation.dao;


import com.gzsf.operation.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int update(User user);
    User getUserById(Long userId);
    User login(@Param("userName")String userName,@Param("password")String password);
}
