package com.jxm.yiti.req;

public class CategorySaveReq {
    private Integer id;

    private Integer parent;

    private String name;

    private Integer level;

    private Integer type;

    private Integer sort;

    private String avatarLink;

    public Integer getParent() {
        return parent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "CategorySaveReq{" +
                "id=" + id +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", type=" + type +
                ", sort=" + sort +
                ", avatarLink='" + avatarLink + '\'' +
                '}';
    }
}