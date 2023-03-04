package com.jxm.yiti.req;

import jakarta.validation.constraints.NotNull;

public class CourseItemSaveReq {
    private Long id;

    @NotNull(message = "[所属课程] 不能为空")
    private String course;

    private Integer sort;

    private String videoLink;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
        return "CourseItemSaveReq{" +
                "id=" + id +
                ", course='" + course + '\'' +
                ", sort=" + sort +
                ", videoLink='" + videoLink + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}