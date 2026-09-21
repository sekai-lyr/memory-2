package com.youkeda.application.ebusiness.service;

import com.youkeda.application.ebusiness.dataobject.OrderDO;
import com.youkeda.application.ebusiness.model.Result;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * 创建订单（直接购买或购物车结算）
     * @param buyerId 买家
     * @param productId 商品
     * @param quantity 数量
     * @param receiver 收货信息 {name, phone, address}
     */
    Result<OrderDO> createOrder(Long buyerId, Long productId, Integer quantity, Map<String, String> receiver);

    /**
     * 模拟支付：PENDING -> PAID，扣减库存
     */
    Result<OrderDO> pay(Long userId, Long orderId);

    /**
     * 取消订单：回补库存（已支付）或直接取消
     */
    Result<OrderDO> cancel(Long userId, Long orderId);

    /**
     * 卖家发货：PAID -> SHIPPED
     */
    Result<OrderDO> ship(Long userId, Long orderId);

    /**
     * 买家确认收货：SHIPPED -> DONE
     */
    Result<OrderDO> confirm(Long userId, Long orderId);

    List<OrderDO> listByBuyer(Long buyerId, String status);

    List<OrderDO> listBySeller(Long sellerId, String status);
}
