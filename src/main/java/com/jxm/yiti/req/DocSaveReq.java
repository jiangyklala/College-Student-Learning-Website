package com.jxm.yiti.req;

import jakarta.validation.constraints.NotNull;

public class DocSaveReq {
    private Long id;

    @NotNull(message = "[所属专栏] 不能为空")
    private Long columnId;

    @NotNull(message = "[父文档] 不能为空")
    private Long parent;

    @NotNull(message = "[文档名称] 不能为空")
    private String name;

    @NotNull(message = "[文档排序] 不能为空")
    private Integer sort;

    @NotNull(message = "[内容] 不能为空")
    private String content;               // docContent 表中的大字段

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", columnId=" + columnId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", content='" + content + '\'' +
                '}';
    }
}