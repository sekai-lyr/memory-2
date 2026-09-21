package com.youkeda.application.ebusiness.service.impl;

import com.youkeda.application.ebusiness.dao.OrderDAO;
import com.youkeda.application.ebusiness.dao.ProductDAO;
import com.youkeda.application.ebusiness.dataobject.OrderDO;
import com.youkeda.application.ebusiness.dataobject.ProductDO;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ProductDAO productDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDO> createOrder(Long buyerId, Long productId, Integer quantity, Map<String, String> receiver) {
        Result<OrderDO> result = new Result<>();
        if (productId == null) {
            return fail("400", "商品不能为空");
        }
        if (quantity == null || quantity < 1) {
            return fail("400", "购买数量不正确");
        }
        ProductDO product = productDAO.selectById(productId);
        if (product == null) {
            return fail("404", "商品不存在");
        }
        if (!"ON".equals(product.getStatus())) {
            return fail("400", "商品已下架");
        }
        if (product.getStock() == null || product.getStock() < quantity) {
            return fail("400", "库存不足，当前仅剩 " + (product.getStock() == null ? 0 : product.getStock()) + " 件");
        }
        if (buyerId.equals(product.getUserId())) {
            return fail("400", "不能购买自己发布的商品");
        }

        OrderDO order = new OrderDO();
        order.setOrderNo(generateOrderNo());
        order.setBuyerId(buyerId);
        order.setSellerId(product.getUserId());
        order.setProductId(productId);
        order.setProductName(product.getName());
        order.setProductImage(firstImage(product.getImages()));
        order.setPrice(product.getPrice());
        order.setQuantity(quantity);
        order.setTotalAmount(round2(product.getPrice() * quantity));
        order.setStatus("PENDING");
        if (receiver != null) {
            order.setReceiverName(receiver.get("name"));
            order.setReceiverPhone(receiver.get("phone"));
            order.setReceiverAddress(receiver.get("address"));
        }
        orderDAO.insert(order);

        result.setCode("200");
        result.setMessage("订单创建成功，请尽快支付");
        result.setSuccess(true);
        result.setData(order);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDO> pay(Long userId, Long orderId) {
        Result<OrderDO> result = new Result<>();
        OrderDO order = orderDAO.selectById(orderId);
        if (order == null) {
            return fail("404", "订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            return fail("403", "无权操作该订单");
        }
        if (!"PENDING".equals(order.getStatus())) {
            return fail("400", "订单状态已变更，无法支付");
        }
        // 扣减库存（带库存校验，防止超卖）
        int rows = productDAO.deductStock(order.getProductId(), order.getQuantity());
        if (rows == 0) {
            ProductDO product = productDAO.selectById(order.getProductId());
            return fail("400", "库存不足，当前仅剩 " + (product == null || product.getStock() == null ? 0 : product.getStock()) + " 件");
        }
        orderDAO.updateStatus(orderId, "PAID");
        order.setStatus("PAID");

        result.setCode("200");
        result.setMessage("支付成功");
        result.setSuccess(true);
        result.setData(order);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDO> cancel(Long userId, Long orderId) {
        Result<OrderDO> result = new Result<>();
        OrderDO order = orderDAO.selectById(orderId);
        if (order == null) {
            return fail("404", "订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            return fail("403", "无权操作该订单");
        }
        String status = order.getStatus();
        if (!"PENDING".equals(status) && !"PAID".equals(status)) {
            return fail("400", "当前状态不可取消");
        }
        // 已支付则回补库存
        if ("PAID".equals(status)) {
            restock(order);
        }
        orderDAO.updateStatus(orderId, "CANCELLED");
        order.setStatus("CANCELLED");

        result.setCode("200");
        result.setMessage("订单已取消");
        result.setSuccess(true);
        result.setData(order);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDO> ship(Long userId, Long orderId) {
        Result<OrderDO> result = new Result<>();
        OrderDO order = orderDAO.selectById(orderId);
        if (order == null) {
            return fail("404", "订单不存在");
        }
        if (!order.getSellerId().equals(userId)) {
            return fail("403", "只有卖家才能发货");
        }
        if (!"PAID".equals(order.getStatus())) {
            return fail("400", "订单还未支付，无法发货");
        }
        orderDAO.updateStatus(orderId, "SHIPPED");
        order.setStatus("SHIPPED");

        result.setCode("200");
        result.setMessage("发货成功");
        result.setSuccess(true);
        result.setData(order);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderDO> confirm(Long userId, Long orderId) {
        Result<OrderDO> result = new Result<>();
        OrderDO order = orderDAO.selectById(orderId);
        if (order == null) {
            return fail("404", "订单不存在");
        }
        if (!order.getBuyerId().equals(userId)) {
            return fail("403", "只有买家才能确认收货");
        }
        if (!"SHIPPED".equals(order.getStatus())) {
            return fail("400", "订单还未发货，无法确认收货");
        }
        orderDAO.updateStatus(orderId, "DONE");
        order.setStatus("DONE");

        result.setCode("200");
        result.setMessage("确认收货成功");
        result.setSuccess(true);
        result.setData(order);
        return result;
    }

    @Override
    public List<OrderDO> listByBuyer(Long buyerId, String status) {
        return orderDAO.selectByBuyer(buyerId, status);
    }

    @Override
    public List<OrderDO> listBySeller(Long sellerId, String status) {
        return orderDAO.selectBySeller(sellerId, status);
    }

    private void restock(OrderDO order) {
        ProductDO product = productDAO.selectById(order.getProductId());
        if (product == null) {
            return;
        }
        int newStock = (product.getStock() == null ? 0 : product.getStock()) + order.getQuantity();
        product.setStock(newStock);
        productDAO.updateStock(product);
    }

    private String generateOrderNo() {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(100000, 999999);
        return "SK" + time + random;
    }

    private String firstImage(String images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        String trimmed = images.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            String inner = trimmed.substring(1, trimmed.length() - 1).trim();
            if (inner.isEmpty()) {
                return null;
            }
            String first = inner.split(",")[0].trim();
            return first.replace("\"", "");
        }
        return trimmed;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private <T> Result<T> fail(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
