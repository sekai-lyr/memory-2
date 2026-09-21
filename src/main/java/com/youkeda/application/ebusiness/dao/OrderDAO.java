package com.youkeda.application.ebusiness.dao;

import com.youkeda.application.ebusiness.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDAO {

    int insert(OrderDO orderDO);

    OrderDO selectById(Long id);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    List<OrderDO> selectByBuyer(@Param("buyerId") Long buyerId, @Param("status") String status);

    List<OrderDO> selectBySeller(@Param("sellerId") Long sellerId, @Param("status") String status);
}
