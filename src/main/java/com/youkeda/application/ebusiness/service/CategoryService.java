package com.youkeda.application.ebusiness.service;

import com.youkeda.application.ebusiness.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {

    Category add(Category category);

    List<Category> queryAll();

    Category get(Long id,boolean deppFill);

}
