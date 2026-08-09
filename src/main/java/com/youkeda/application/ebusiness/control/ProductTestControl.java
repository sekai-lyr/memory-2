package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.model.Paging;
import com.youkeda.application.ebusiness.model.Product;
import com.youkeda.application.ebusiness.model.Result;
import com.youkeda.application.ebusiness.param.BasePageParam;
import com.youkeda.application.ebusiness.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductTestControl {

    @Autowired
    private ProductService productService;

    @GetMapping("/publish")
    public String publish() {
        return "product";
    }

    @PostMapping("/pub/api")
    @ResponseBody
    public Result<Product> publishAction(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        Result<Product> result = new Result<>();
        result.setCode("200");
        result.setMessage("恭喜你，商品已经成功存入");
        result.setSuccess(true);
        result.setData(savedProduct);
        return result;
    }

    @GetMapping("/list")
    public String listPage() {
        return "product-list";
    }

    @GetMapping("/list/api")
    @ResponseBody
    public Result<Paging<Product>> listAction(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        BasePageParam param = new BasePageParam();
        param.setPagination(pageNum);
        param.setPageSize(pageSize);

        Paging<Product> paging = productService.pageQueryProduct(param);
        Result<Paging<Product>> result = new Result<>();
        result.setCode("200");
        result.setMessage("商品列表查询成功");
        result.setSuccess(true);
        result.setData(paging);
        return result;
    }
}
