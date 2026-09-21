package com.youkeda.application.ebusiness.service.impl;

import com.youkeda.application.ebusiness.dao.ProductDAO;
import com.youkeda.application.ebusiness.dataobject.ProductDO;
import com.youkeda.application.ebusiness.model.Paging;
import com.youkeda.application.ebusiness.model.Product;
import com.youkeda.application.ebusiness.param.BasePageParam;
import com.youkeda.application.ebusiness.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Override
    public Product save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("商品数据不能为空");
        }
        validate(product);

        ProductDO productDO = new ProductDO(product);
        productDO.setUserId(product.getUserId() == null ? 1L : product.getUserId());
        productDO.setStatus(StringUtils.hasText(product.getStatus()) ? product.getStatus().trim() : "ON");
        productDO.setImages(normalizeStringArray(product.getImages()));
        productDO.setDetail(normalizeStringArray(product.getDetail()));
        productDO.setCategoryIds(normalizeNumberArray(product.getCategoryIds()));

        productDAO.insert(productDO);
        return productDO.convertToModel();
    }

    @Override
    public Product update(Product product) {
        if (product == null || product.getId() == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        validate(product);
        ProductDO exist = productDAO.selectById(product.getId());
        if (exist == null) {
            throw new IllegalArgumentException("商品不存在");
        }

        ProductDO productDO = new ProductDO(product);
        productDO.setUserId(exist.getUserId());
        productDO.setStatus(exist.getStatus());
        productDO.setImages(normalizeStringArray(product.getImages()));
        productDO.setDetail(normalizeStringArray(product.getDetail()));
        productDO.setCategoryIds(normalizeNumberArray(product.getCategoryIds()));

        productDAO.updateProduct(productDO);
        return productDO.convertToModel();
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        if (id == null) {
            return false;
        }
        String safe = "ON".equals(status) ? "ON" : "OFF";
        return productDAO.updateStatus(id, safe) > 0;
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        return productDAO.deleteById(id) > 0;
    }

    @Override
    public Product getById(Long id) {
        ProductDO productDO = productDAO.selectById(id);
        return productDO == null ? null : productDO.convertToModel();
    }

    @Override
    public Paging<Product> pageQueryProduct(BasePageParam param) {
        return pageQueryInternal(param, productDAO::selectAllCounts, productDAO::pageQuery);
    }

    @Override
    public Paging<Product> pageQueryOnSale(BasePageParam param) {
        return pageQueryInternal(param, productDAO::selectOnCounts, productDAO::pageQueryOnSale);
    }

    @Override
    public Paging<Product> pageQueryMine(Long userId, BasePageParam param) {
        if (userId == null) {
            throw new IllegalArgumentException("请先登录");
        }
        BasePageParam safeParam = safeParam(param);
        int totalCount = productDAO.selectCountByUserId(userId);
        List<ProductDO> productDOList = productDAO.pageQueryByUserId(withUser(safeParam, userId));
        return buildPaging(safeParam, totalCount, productDOList);
    }

    private interface CountProvider {
        int count();
    }

    private interface ListProvider {
        List<ProductDO> list(BasePageParam param);
    }

    private Paging<Product> pageQueryInternal(BasePageParam param, CountProvider countProvider, ListProvider listProvider) {
        BasePageParam safeParam = safeParam(param);
        int totalCount = countProvider.count();
        List<ProductDO> productDOList = listProvider.list(safeParam);
        return buildPaging(safeParam, totalCount, productDOList);
    }

    private Paging<Product> buildPaging(BasePageParam safeParam, int totalCount, List<ProductDO> productDOList) {
        List<Product> productList = new ArrayList<>();
        if (productDOList != null) {
            for (ProductDO productDO : productDOList) {
                productList.add(productDO.convertToModel());
            }
        }
        int totalPage = totalCount == 0 ? 0 : (totalCount + safeParam.getPageSize() - 1) / safeParam.getPageSize();

        Paging<Product> paging = new Paging<>();
        paging.setPageNum(safeParam.getPagination());
        paging.setPageSize(safeParam.getPageSize());
        paging.setTotalCount(totalCount);
        paging.setTotalPage(totalPage);
        paging.setData(productList);
        return paging;
    }

    private BasePageParam safeParam(BasePageParam param) {
        BasePageParam safeParam = param == null ? new BasePageParam() : param;
        if (safeParam.getPagination() <= 0) {
            safeParam.setPagination(1);
        }
        if (safeParam.getPageSize() <= 0) {
            safeParam.setPageSize(10);
        }
        return safeParam;
    }

    private BasePageParam withUser(BasePageParam param, Long userId) {
        BasePageParam p = new BasePageParam();
        p.setPagination(param.getPagination());
        p.setPageSize(param.getPageSize());
        p.setUserId(userId);
        return p;
    }

    private void validate(Product product) {
        if (!StringUtils.hasText(product.getName())) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("商品价格不能为空且不能小于0");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("库存不能为空且不能小于0");
        }
    }

    private String normalizeStringArray(String raw) {
        if (!StringUtils.hasText(raw)) {
            return "[]";
        }
        String value = raw.trim();
        if (value.startsWith("[") && value.endsWith("]")) {
            return value;
        }
        String[] parts = value.split(",");
        List<String> cleaned = new ArrayList<>();
        for (String part : parts) {
            String item = part == null ? "" : part.trim();
            if (!item.isEmpty()) {
                item = item.replace("\\", "\\\\").replace("\"", "\\\"");
                cleaned.add("\"" + item + "\"");
            }
        }
        return "[" + String.join(",", cleaned) + "]";
    }

    private String normalizeNumberArray(String raw) {
        if (!StringUtils.hasText(raw)) {
            return "[]";
        }
        String value = raw.trim();
        if (value.startsWith("[") && value.endsWith("]")) {
            return value;
        }
        String[] parts = value.split(",");
        List<String> cleaned = new ArrayList<>();
        for (String part : parts) {
            String item = part == null ? "" : part.trim();
            if (!item.isEmpty()) {
                cleaned.add(item);
            }
        }
        return "[" + String.join(",", cleaned) + "]";
    }
}
