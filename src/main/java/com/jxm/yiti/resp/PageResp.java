package com.jxm.yiti.resp;

import java.util.List;

public class PageResp<T> {

    private long total;

    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public java.util.List<T> getList() {
        return list;
    }

    public void setList(java.util.List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", List=" + list +
                '}';
    }
}