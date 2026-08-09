package com.youkeda.application.ebusiness.model;

import com.youkeda.application.ebusiness.dataobject.ProductDO;

import java.io.Serializable;
import java.util.List;

public class Paging <R> implements Serializable {

    private int pageNum=1;

    private int pageSize=15;

    private int totalCount;

    private int totalPage;

    private List<R> data;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<R> getData() {
        return data;
    }

    public void setData(List<R> data) {
        this.data = data;
    }

    public Paging() {

    }
    public Paging(int pageNum, int pageSize, int totalCount, int totalPage, List<R> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.data = data;
    }
}
