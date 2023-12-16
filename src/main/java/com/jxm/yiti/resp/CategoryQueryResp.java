package com.jxm.yiti.resp;

public class CategoryQueryResp {
    private Integer id;

    private Integer parent;

    private String name;

    private Integer level;

    private Integer total;

    private Integer sort;

    private String avatarLink;

    private Integer type;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "CategoryQueryResp{" +
                "id=" + id +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", total=" + total +
                ", sort=" + sort +
                ", avatarLink='" + avatarLink + '\'' +
                ", type=" + type +
                '}';
    }
}