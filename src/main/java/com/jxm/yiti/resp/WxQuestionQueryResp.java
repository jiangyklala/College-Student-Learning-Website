package com.jxm.yiti.resp;

public class WxQuestionQueryResp {
    private String title;

    private Long categoryId;

    private Integer like;

    private Integer collect;

    private Integer points;

    private Integer importanceLevel;

    private Integer answerId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getCollect() {
        return collect;
    }

    public void setCollect(Integer collect) {
        this.collect = collect;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getImportanceLevel() {
        return importanceLevel;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public void setImportanceLevel(Integer importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    @Override
    public String toString() {
        return "WxQuestionQueryResp{" +
                "title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", like=" + like +
                ", collect=" + collect +
                ", points=" + points +
                ", importanceLevel=" + importanceLevel +
                ", answerId=" + answerId +
                '}';
    }
}