package com.jxm.yiti.req;

public class QuestionDetailQueryReq extends PageReq {
    private Long id;

    private String name;

    private Integer type;

    private Long categoryId1;

    private Long categoryId2;

    private Integer level;

    private String content;

    private Integer answer;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QuestionDetailQueryReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", level=" + level +
                ", content='" + content + '\'' +
                ", answer=" + answer +
                '}';
    }
}