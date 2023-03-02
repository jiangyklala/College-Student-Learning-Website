package com.jxm.yiti.req;

import jakarta.validation.constraints.NotNull;

public class CourseListSaveReq {
    private Long id;

    @NotNull(message = "[名称] 不能为空")
    private String name;

    private String avatarLink;

    private Long categoryId1;

    private Long categoryId2;

    private Integer clickCount;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public Long getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(Long categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CourseListSaveReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatarLink='" + avatarLink + '\'' +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", clickCount=" + clickCount +
                ", description='" + description + '\'' +
                '}';
    }
}
