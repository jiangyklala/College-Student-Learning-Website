package com.jxm.yiti.req;

public class CourseItemQueryReq extends PageReq {

    private String course;

    private Integer sort;

    private String videoLink;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", course=").append(course);
        sb.append(", sort=").append(sort);
        sb.append(", videoLink=").append(videoLink);
        sb.append("]");
        return sb.toString();
    }
}