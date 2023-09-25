package com.jxm.yiti.req;

public class CategoryQueryReq extends PageReq {

    private String name;

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                "} " + super.toString();
    }
}