package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.model.Category;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryTestControl {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/list/api")
    public Result<List<Category>> listAll() {
        List<Category> categories = categoryService.queryAll();
        Result<List<Category>> result = new Result<>();
        result.setCode("200");
        result.setMessage("分类列表查询成功");
        result.setSuccess(true);
        result.setData(categories);
        return result;
    }

    @GetMapping(path = "/test/category")
    java.util.Map<String, Object> testCategory() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("code", 200);
        result.put("message", "恭喜你！接口访问成功啦！");
        return result;
    }
}
