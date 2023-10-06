package com.jxm.yiti.resp;

public class WxUserInfoResp {

    private Integer points;

    private String name;

    private Integer type;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", points=").append(points);
        sb.append(", other1=").append(name);
        sb.append(", other2=").append(type);
        sb.append("]");
        return sb.toString();
    }
}