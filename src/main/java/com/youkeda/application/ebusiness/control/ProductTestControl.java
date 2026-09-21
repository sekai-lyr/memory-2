package com.youkeda.application.ebusiness.control;

import com.youkeda.application.ebusiness.config.LoginContext;
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

import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductTestControl {

    @Autowired
    private ProductService productService;

    @Autowired
    private LoginContext loginContext;

    @GetMapping("/publish")
    public String publish() {
        return "product";
    }

    @GetMapping("/edit")
    public String edit() {
        return "product";
    }

    @GetMapping("/detail")
    public String detail() {
        return "product-detail";
    }

    @GetMapping("/list")
    public String listPage() {
        return "product-list";
    }

    @GetMapping("/mine")
    public String minePage() {
        return "my-products";
    }

    /**
     * 发布商品（必须登录，userId 取自会话）
     */
    @PostMapping("/pub/api")
    @ResponseBody
    public Result<Product> publishAction(@RequestBody Product product) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        product.setUserId(userId);
        Product savedProduct = productService.save(product);
        Result<Product> result = new Result<>();
        result.setCode("200");
        result.setMessage("恭喜你，商品已经成功发布");
        result.setSuccess(true);
        result.setData(savedProduct);
        return result;
    }

    /**
     * 更新商品（仅本人）
     */
    @PostMapping("/update/api")
    @ResponseBody
    public Result<Product> updateAction(@RequestBody Product product) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Product exist = productService.getById(product.getId());
        if (exist == null) {
            return fail("400", "商品不存在");
        }
        if (!userId.equals(exist.getUserId())) {
            return fail("403", "只能编辑自己发布的商品");
        }
        Product updated = productService.update(product);
        Result<Product> result = new Result<>();
        result.setCode("200");
        result.setMessage("商品更新成功");
        result.setSuccess(true);
        result.setData(updated);
        return result;
    }

    /**
     * 上架/下架（仅本人）
     */
    @PostMapping("/status/api")
    @ResponseBody
    public Result<Void> statusAction(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long id = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        String status = body.get("status") == null ? null : body.get("status").toString();
        Product exist = productService.getById(id);
        if (exist == null) {
            return fail("400", "商品不存在");
        }
        if (!userId.equals(exist.getUserId())) {
            return fail("403", "只能操作自己发布的商品");
        }
        if (!productService.updateStatus(id, status)) {
            return fail("500", "操作失败");
        }
        return ok("操作成功");
    }

    /**
     * 删除商品（仅本人）
     */
    @PostMapping("/delete/api")
    @ResponseBody
    public Result<Void> deleteAction(@RequestBody Map<String, Object> body) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        Long id = body.get("id") == null ? null : Long.valueOf(body.get("id").toString());
        Product exist = productService.getById(id);
        if (exist == null) {
            return fail("400", "商品不存在");
        }
        if (!userId.equals(exist.getUserId())) {
            return fail("403", "只能删除自己发布的商品");
        }
        if (!productService.delete(id)) {
            return fail("500", "删除失败");
        }
        return ok("删除成功");
    }

    /**
     * 商品详情
     */
    @GetMapping("/detail/api")
    @ResponseBody
    public Result<Product> detail(@RequestParam Long id) {
        Product product = productService.getById(id);
        Result<Product> result = new Result<>();
        if (product == null) {
            result.setCode("404");
            result.setMessage("商品不存在或已下架");
            result.setSuccess(false);
            return result;
        }
        result.setCode("200");
        result.setMessage("查询成功");
        result.setSuccess(true);
        result.setData(product);
        return result;
    }

    /**
     * 市场：在售商品分页
     */
    @GetMapping("/list/api")
    @ResponseBody
    public Result<Paging<Product>> listAction(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        BasePageParam param = new BasePageParam();
        param.setPagination(pageNum);
        param.setPageSize(pageSize);

        Paging<Product> paging = productService.pageQueryOnSale(param);
        Result<Paging<Product>> result = new Result<>();
        result.setCode("200");
        result.setMessage("商品列表查询成功");
        result.setSuccess(true);
        result.setData(paging);
        return result;
    }

    /**
     * 我的商品分页（登录）
     */
    @GetMapping("/mine/api")
    @ResponseBody
    public Result<Paging<Product>> mineAction(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = loginContext.currentUserId();
        if (userId == null) {
            return needLogin();
        }
        BasePageParam param = new BasePageParam();
        param.setPagination(pageNum);
        param.setPageSize(pageSize);

        Paging<Product> paging = productService.pageQueryMine(userId, param);
        Result<Paging<Product>> result = new Result<>();
        result.setCode("200");
        result.setMessage("查询成功");
        result.setSuccess(true);
        result.setData(paging);
        return result;
    }

    private <T> Result<T> needLogin() {
        Result<T> result = new Result<>();
        result.setCode("401");
        result.setMessage("请先登录");
        result.setSuccess(false);
        return result;
    }

    private <T> Result<T> ok(String message) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

    private <T> Result<T> fail(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
