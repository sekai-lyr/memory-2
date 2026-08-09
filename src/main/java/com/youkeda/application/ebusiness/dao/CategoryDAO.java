package com.youkeda.application.ebusiness.dao;

import com.youkeda.application.ebusiness.dataobject.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryDAO {

    List<CategoryDO> selectAll();

    CategoryDO selectById(Long id);

    List<CategoryDO> selectByParentId(Long parentCategoryId);

    int insert(CategoryDO categoryDO);
}
