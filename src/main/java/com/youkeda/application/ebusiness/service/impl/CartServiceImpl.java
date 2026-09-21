package com.youkeda.application.ebusiness.service.impl;

import com.youkeda.application.ebusiness.dao.CartDAO;
import com.youkeda.application.ebusiness.dao.ProductDAO;
import com.youkeda.application.ebusiness.dataobject.CartDO;
import com.youkeda.application.ebusiness.dataobject.ProductDO;
import com.youkeda.application.ebusiness.model.CartVO;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private ProductDAO productDAO;

    @Override
    public Result<CartVO> add(Long userId, Long productId, Integer quantity) {
        Result<CartVO> result = new Result<>();
        if (productId == null) {
            return fail("400", "商品不能为空");
        }
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }
        ProductDO product = productDAO.selectById(productId);
        if (product == null) {
            return fail("400", "商品不存在");
        }
        if (!"ON".equals(product.getStatus())) {
            return fail("400", "商品已下架，无法加入购物车");
        }
        if (quantity > product.getStock()) {
            return fail("400", "库存不足，当前仅剩 " + product.getStock() + " 件");
        }

        CartDO exist = cartDAO.selectByUserAndProduct(userId, productId);
        if (exist != null) {
            int newQuantity = exist.getQuantity() + quantity;
            if (newQuantity > product.getStock()) {
                return fail("400", "库存不足，当前仅剩 " + product.getStock() + " 件");
            }
            cartDAO.updateQuantity(exist.getId(), newQuantity);
            result.setCode("200");
            result.setMessage("已加入购物车");
            result.setSuccess(true);
            result.setData(convert(exist.getId(), product, newQuantity));
            return result;
        }

        CartDO cartDO = new CartDO();
        cartDO.setUserId(userId);
        cartDO.setProductId(productId);
        cartDO.setQuantity(quantity);
        cartDAO.insert(cartDO);
        result.setCode("200");
        result.setMessage("已加入购物车");
        result.setSuccess(true);
        result.setData(convert(cartDO.getId(), product, quantity));
        return result;
    }

    @Override
    public Result<CartVO> updateQuantity(Long userId, Long cartId, Integer quantity) {
        Result<CartVO> result = new Result<>();
        CartDO cart = cartDAO.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            return fail("404", "购物车条目不存在");
        }
        if (quantity == null || quantity < 1) {
            quantity = 1;
        }
        ProductDO product = productDAO.selectById(cart.getProductId());
        if (product != null && quantity > product.getStock()) {
            return fail("400", "库存不足，当前仅剩 " + product.getStock() + " 件");
        }
        cartDAO.updateQuantity(cartId, quantity);
        result.setCode("200");
        result.setMessage("已更新");
        result.setSuccess(true);
        result.setData(convert(cartId, product, quantity));
        return result;
    }

    @Override
    public Result<Void> remove(Long userId, Long cartId) {
        Result<Void> result = new Result<>();
        CartDO cart = cartDAO.selectById(cartId);
        if (cart == null || !cart.getUserId().equals(userId)) {
            return fail("404", "购物车条目不存在");
        }
        cartDAO.deleteById(cartId);
        result.setCode("200");
        result.setMessage("已删除");
        result.setSuccess(true);
        return result;
    }

    @Override
    public Result<Void> clear(Long userId) {
        Result<Void> result = new Result<>();
        cartDAO.clearByUser(userId);
        result.setCode("200");
        result.setMessage("购物车已清空");
        result.setSuccess(true);
        return result;
    }

    @Override
    public List<CartVO> list(Long userId) {
        List<CartVO> list = cartDAO.selectCartList(userId);
        if (list != null) {
            for (CartVO vo : list) {
                vo.setProductImage(firstImage(vo.getProductImage()));
            }
        }
        return list;
    }

    private CartVO convert(Long cartId, ProductDO product, Integer quantity) {
        CartVO vo = new CartVO();
        vo.setId(cartId);
        vo.setProductId(product.getId());
        vo.setQuantity(quantity);
        vo.setProductName(product.getName());
        vo.setProductImage(firstImage(product.getImages()));
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setStatus(product.getStatus());
        return vo;
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

    private <T> Result<T> fail(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
