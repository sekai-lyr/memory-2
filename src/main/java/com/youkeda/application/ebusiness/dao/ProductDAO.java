package com.youkeda.application.ebusiness.dao;

import com.youkeda.application.ebusiness.dataobject.ProductDO;
import com.youkeda.application.ebusiness.model.Product;
import com.youkeda.application.ebusiness.param.BasePageParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDAO {

    int insert(ProductDO productDO);

    ProductDO selectByName(String name);

    ProductDO selectById(Long id);

    int updateStock(ProductDO productDO);

    int selectAllCounts();

    List<ProductDO> pageQuery(BasePageParam param);

}
