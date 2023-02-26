package com.jxm.yiti.req;

public class CourseListQueryReq extends PageReq {

    private String name;

    private String avatar;

    private Long categoryId1;

    private Long categoryId2;

    private Integer clickCount;

    private String videoSize;

    private String videoLink;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CourseListQueryReq{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", clickCount=" + clickCount +
                ", videoSize='" + videoSize + '\'' +
                ", videoLink='" + videoLink + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
