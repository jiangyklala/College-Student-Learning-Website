package com.jxm.yiti.req;

import jakarta.validation.constraints.NotNull;

public class ColumnListSaveReq {
    private Long id;

    @NotNull(message = "[专栏名称] 不能为空")
    private String name;

    @NotNull(message = "[分类一] 不能为空")
    private Long categoryId1;

    private Long categoryId2;

    private String description;

    private String avatarLink;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "ColumnListSaveReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", description='" + description + '\'' +
                ", avatarLink='" + avatarLink + '\'' +
                ", docCount=" + docCount +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                '}';
    }
}