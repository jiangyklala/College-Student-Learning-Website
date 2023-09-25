package com.jxm.yiti.req;

public class WxQuestionSaveReq {
    private Integer id;

    private String title;

    private Integer categoryId;

    private Integer like;

    private Integer collect;

    private Integer points;

    private Integer importanceLevel;

    private String answer;

    private Long answerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
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

    public void setImportanceLevel(Integer importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    @Override
    public String toString() {
        return "WxQuestionSaveReq{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", like=" + like +
                ", collect=" + collect +
                ", points=" + points +
                ", importanceLevel=" + importanceLevel +
                ", answer='" + answer + '\'' +
                ", answerId=" + answerId +
                '}';
    }
}