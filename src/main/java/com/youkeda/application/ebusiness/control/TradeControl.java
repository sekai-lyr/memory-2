package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.config.LoginContext;
import com.youkeda.application.ebusiness.dataobject.OrderDO;
import com.youkeda.application.ebusiness.model.CartVO;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.service.CartService;
import com.youkeda.application.ebusiness.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trade")
public class TradeControl {

    @Autowired
    private LoginContext loginContext;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    /* ─── 页面 ─── */

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }

    @GetMapping("/seller/orders")
    public String sellerOrdersPage() {
        return "seller-orders";
    }

    /* ─── 购物车 ─── */

    @GetMapping("/cart/list/api")
    @ResponseBody
    public Result<List<CartVO>> cartList() {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Result<List<CartVO>> result = new Result<>();
        result.setCode("200");
        result.setMessage("ok");
        result.setSuccess(true);
        result.setData(cartService.list(userId));
        return result;
    }

    @PostMapping("/cart/add/api")
    @ResponseBody
    public Result<CartVO> cartAdd(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long productId = body.get("productId") == null ? null : Long.valueOf(body.get("productId").toString());
        Integer quantity = body.get("quantity") == null ? 1 : Integer.valueOf(body.get("quantity").toString());
        return cartService.add(userId, productId, quantity);
    }

    @PostMapping("/cart/update/api")
    @ResponseBody
    public Result<CartVO> cartUpdate(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long cartId = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        Integer quantity = body.get("quantity") == null ? 1 : Integer.valueOf(body.get("quantity").toString());
        return cartService.updateQuantity(userId, cartId, quantity);
    }

    @PostMapping("/cart/remove/api")
    @ResponseBody
    public Result<Void> cartRemove(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long cartId = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        return cartService.remove(userId, cartId);
    }

    @PostMapping("/cart/clear/api")
    @ResponseBody
    public Result<Void> cartClear() {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        return cartService.clear(userId);
    }

    /* ─── 订单 ─── */

    /**
     * 立即购买 / 结算：body = {productId, quantity, receiver:{name,phone,address}}
     */
    @PostMapping("/order/create/api")
    @ResponseBody
    public Result<OrderDO> createOrder(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long productId = body.get("productId") == null ? null : Long.valueOf(body.get("productId").toString());
        Integer quantity = body.get("quantity") == null ? 1 : Integer.valueOf(body.get("quantity").toString());
        @SuppressWarnings("unchecked")
        Map<String, String> receiver = (Map<String, String>) body.get("receiver");
        return orderService.createOrder(userId, productId, quantity, receiver);
    }

    /**
     * 购物车结算：body = {cartIds:[1,2,3], receiver:{...}}
     */
    @PostMapping("/order/checkout/api")
    @ResponseBody
    public Result<List<OrderDO>> checkout(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        @SuppressWarnings("unchecked")
        List<Number> cartIdsRaw = (List<Number>) body.get("cartIds");
        @SuppressWarnings("unchecked")
        Map<String, String> receiver = (Map<String, String>) body.get("receiver");
        List<Long> cartIds = new ArrayList<>();
        if (cartIdsRaw != null) {
            for (Number n : cartIdsRaw) {
                cartIds.add(n.longValue());
            }
        }
        if (cartIds.isEmpty()) {
            return fail("400", "请选择要结算的商品");
        }

        List<CartVO> cartList = cartService.list(userId);
        List<OrderDO> orders = new ArrayList<>();
        for (CartVO item : cartList) {
            if (!cartIds.contains(item.getId())) {
                continue;
            }
            if (!"ON".equals(item.getStatus())) {
                return fail("400", "商品「" + item.getProductName() + "」已下架，请从购物车移除");
            }
            Result<OrderDO> r = orderService.createOrder(userId, item.getProductId(), item.getQuantity(), receiver);
            if (!r.isSuccess()) {
                return fail("400", r.getMessage());
            }
            orders.add(r.getData());
            cartService.remove(userId, item.getId());
        }
        if (orders.isEmpty()) {
            return fail("400", "没有可结算的商品");
        }

        Result<List<OrderDO>> result = new Result<>();
        result.setCode("200");
        result.setMessage("下单成功，共 " + orders.size() + " 笔订单，请尽快支付");
        result.setSuccess(true);
        result.setData(orders);
        return result;
    }

    @PostMapping("/order/pay/api")
    @ResponseBody
    public Result<OrderDO> pay(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long orderId = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        return orderService.pay(userId, orderId);
    }

    @PostMapping("/order/cancel/api")
    @ResponseBody
    public Result<OrderDO> cancel(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long orderId = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        return orderService.cancel(userId, orderId);
    }

    @PostMapping("/order/ship/api")
    @ResponseBody
    public Result<OrderDO> ship(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long orderId = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        return orderService.ship(userId, orderId);
    }

    @PostMapping("/order/confirm/api")
    @ResponseBody
    public Result<OrderDO> confirm(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long orderId = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        return orderService.confirm(userId, orderId);
    }

    @GetMapping("/order/list/api")
    @ResponseBody
    public Result<List<OrderDO>> orderList(@RequestParam(required = false) String role,
                                           @RequestParam(required = false) String status) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        List<OrderDO> orders;
        if ("seller".equals(role)) {
            orders = orderService.listBySeller(userId, status);
        } else {
            orders = orderService.listByBuyer(userId, status);
        }
        Result<List<OrderDO>> result = new Result<>();
        result.setCode("200");
        result.setMessage("ok");
        result.setSuccess(true);
        result.setData(orders);
        return result;
    }

    private <T> Result<T> needLogin() {
        Result<T> result = new Result<>();
        result.setCode("401");
        result.setMessage("请先登录");
        result.setSuccess(false);
        return result;
    }

    private <T> Result<T> fail(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
