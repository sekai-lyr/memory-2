package com.youkeda.application.ebusiness.dataobject;

import com.youkeda.application.ebusiness.model.BaseDate;

public class CartDO extends BaseDate {

    private Long userId;

    private Long productId;

    private Integer quantity;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
