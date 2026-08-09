package com.youkeda.application.ebusiness.dataobject;

import com.youkeda.application.ebusiness.model.BaseDate;
import com.youkeda.application.ebusiness.model.Category;
import org.springframework.beans.BeanUtils;

public class CategoryDO extends BaseDate {

    private String name;

    private String description;

    private Long parentCategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public CategoryDO() {}

    public CategoryDO(Category category) {
        if (category != null) {
            BeanUtils.copyProperties(category, this);
        }
    }

    public Category convertToModel() {
        Category category = new Category();
        BeanUtils.copyProperties(this, category);
        return category;
    }
}
