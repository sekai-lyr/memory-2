package com.youkeda.application.ebusiness.dao;

import com.youkeda.application.ebusiness.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDAO {
    int add(UserDO userDO);

    int batchAdd(@Param("list") List<UserDO> userDOList);

    int update(UserDO userDO);

    int delete(@Param("id") Long id);

    UserDO findByUserName(@Param("userName") String userName);

    List<UserDO> findByIds(@Param("ids") List<Long> ids);
}
