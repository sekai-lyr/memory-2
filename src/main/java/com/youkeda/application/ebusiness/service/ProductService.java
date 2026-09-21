package com.youkeda.application.ebusiness.service;

import com.youkeda.application.ebusiness.model.Paging;
import com.youkeda.application.ebusiness.model.Product;
import com.youkeda.application.ebusiness.param.BasePageParam;


public interface ProductService {

    Product save(Product product);

    Product update(Product product);

    boolean updateStatus(Long id, String status);

    boolean delete(Long id);

    Product getById(Long id);

    Paging<Product> pageQueryProduct(BasePageParam param);

    /**
     * 市场：仅查询在售商品
     */
    Paging<Product> pageQueryOnSale(BasePageParam param);

    /**
     * 我的商品
     */
    Paging<Product> pageQueryMine(Long userId, BasePageParam param);
}
