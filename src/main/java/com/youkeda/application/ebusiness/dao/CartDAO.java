package com.youkeda.application.ebusiness.dao;

import com.youkeda.application.ebusiness.dataobject.CartDO;
import com.youkeda.application.ebusiness.model.CartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDAO {

    int insert(CartDO cartDO);

    CartDO selectByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    CartDO selectById(@Param("id") Long id);

    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    int deleteById(@Param("id") Long id);

    int deleteByIds(@Param("ids") List<Long> ids);

    int clearByUser(@Param("userId") Long userId);

    List<CartVO> selectCartList(@Param("userId") Long userId);
}
