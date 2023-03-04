package com.jxm.yiti.domain;

public class CourseItem {
    private String course;

    private Integer sort;

    private Long id;

    private String videoLink;

    private String description;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", course=").append(course);
        sb.append(", sort=").append(sort);
        sb.append(", id=").append(id);
        sb.append(", videoLink=").append(videoLink);
        sb.append(", description=").append(description);
        sb.append("]");
        return sb.toString();
    }
}