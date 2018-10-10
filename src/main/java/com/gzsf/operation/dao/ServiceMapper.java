package com.gzsf.operation.dao;


import com.github.pagehelper.Page;
import com.gzsf.operation.model.ServiceModel;
import com.gzsf.operation.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ServiceMapper {
    int update(ServiceModel serviceModel);

    int insert(ServiceModel serviceModel);

    void delete(Long serviceId);
    Page<ServiceModel> getServices(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

}
