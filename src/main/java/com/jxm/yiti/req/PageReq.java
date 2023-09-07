package com.jxm.yiti.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class PageReq {

    // TODO bugfix
    @NotNull(message = "[当前页] 不能为空")
    private int page;

    @NotNull(message = "[每页条数] 不能为空")
    @Max(value = 1000, message = "[每页条数] 不能超过 1000")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}