package com.youkeda.application.ebusiness.service.impl;

import com.youkeda.application.ebusiness.dao.CategoryDAO;
import com.youkeda.application.ebusiness.dataobject.CategoryDO;
import com.youkeda.application.ebusiness.model.Category;
import com.youkeda.application.ebusiness.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public Category add(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("分类数据不能为空");
        }
        CategoryDO categoryDO = new CategoryDO(category);
        if (categoryDO.getParentCategoryId() == null) {
            categoryDO.setParentCategoryId(0L);
        }
        categoryDAO.insert(categoryDO);
        return categoryDO.convertToModel();
    }

    @Override
    public List<Category> queryAll() {
        List<CategoryDO> categoryDOList = categoryDAO.selectAll();
        List<Category> allCategories = new ArrayList<>();
        if (categoryDOList != null) {
            for (CategoryDO categoryDO : categoryDOList) {
                allCategories.add(categoryDO.convertToModel());
            }
        }
        return allCategories;
    }

    @Override
    public Category get(Long id, boolean deepFill) {
        if (id == null) {
            return null;
        }
        CategoryDO categoryDO = categoryDAO.selectById(id);
        if (categoryDO == null) {
            return null;
        }
        Category category = categoryDO.convertToModel();

        if (deepFill) {
            List<CategoryDO> children = categoryDAO.selectByParentId(id);
            if (children != null && !children.isEmpty()) {
                List<Category> subCategories = new ArrayList<>();
                for (CategoryDO child : children) {
                    subCategories.add(child.convertToModel());
                }
                category.setSubCategories(subCategories);
            }
        }

        return category;
    }
}
