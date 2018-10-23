package com.gzsf.operation.dao;

import com.gzsf.operation.model.IdCardList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdCardListMapper {
    int countByIdCard(String idCard);

}
