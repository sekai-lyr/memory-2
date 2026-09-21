package com.youkeda.application.ebusiness.service;

import com.youkeda.application.ebusiness.model.CartVO;
import com.youkeda.application.ebusiness.model.Result;

import java.util.List;

public interface CartService {

    /**
     * 加入购物车（已存在则累加数量）
     */
    Result<CartVO> add(Long userId, Long productId, Integer quantity);

    /**
     * 修改数量
     */
    Result<CartVO> updateQuantity(Long userId, Long cartId, Integer quantity);

    /**
     * 删除购物车条目
     */
    Result<Void> remove(Long userId, Long cartId);

    /**
     * 清空购物车（结算后调用）
     */
    Result<Void> clear(Long userId);

    List<CartVO> list(Long userId);
}
