package com.youkeda.application.ebusiness.dao;

import com.youkeda.application.ebusiness.dataobject.ProductDO;
import com.youkeda.application.ebusiness.param.BasePageParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDAO {

    int insert(ProductDO productDO);

    ProductDO selectByName(String name);

    ProductDO selectById(Long id);

    int updateStock(ProductDO productDO);

    int deductStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    int updateProduct(ProductDO productDO);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int deleteById(@Param("id") Long id);

    int selectOnCounts();

    List<ProductDO> pageQueryOnSale(BasePageParam param);

    int selectCountByUserId(@Param("userId") Long userId);

    List<ProductDO> pageQueryByUserId(BasePageParam param);

    int selectAllCounts();

    List<ProductDO> pageQuery(BasePageParam param);

}
