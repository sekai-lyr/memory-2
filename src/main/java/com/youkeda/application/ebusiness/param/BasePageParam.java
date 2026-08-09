package com.youkeda.application.ebusiness.param;

public class BasePageParam {

    private int pagination = 1;

    private int pageSize = 10;

    public int getPagination() {
        return pagination;
    }

    public void setPagination(int pagination) {
        this.pagination = pagination;
    }

    public int getPageNum() {
        return pagination;
    }

    public void setPageNum(int pageNum) {
        this.pagination = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        int safePageNum = Math.max(this.pagination, 1);
        int safePageSize = this.pageSize <= 0 ? 10 : this.pageSize;
        return (safePageNum - 1) * safePageSize;
    }
}
