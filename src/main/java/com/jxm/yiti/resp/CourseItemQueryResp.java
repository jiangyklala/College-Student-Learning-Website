package com.jxm.yiti.resp;

public class CourseItemQueryResp {
    private Long id;

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
        return "CourseItemQueryResp{" +
                "id=" + id +
                ", course='" + course + '\'' +
                ", sort=" + sort +
                ", videoLink='" + videoLink + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}