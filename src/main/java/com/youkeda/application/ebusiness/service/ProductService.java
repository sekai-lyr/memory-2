package com.youkeda.application.ebusiness.service;

import com.youkeda.application.ebusiness.model.Paging;
import com.youkeda.application.ebusiness.model.Product;
import com.youkeda.application.ebusiness.param.BasePageParam;
import jakarta.validation.constraints.AssertFalse;
import org.springframework.stereotype.Service;


public interface ProductService {

    Product save(Product product);

    Paging<Product> pageQueryProduct(BasePageParam param);
}
