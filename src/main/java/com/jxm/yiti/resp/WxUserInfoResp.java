package com.jxm.yiti.resp;

public class WxUserInfoResp {

    private Integer points;

    private String other1;

    private Integer type;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
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
        sb.append(", other1=").append(other1);
        sb.append(", other2=").append(type);
        sb.append("]");
        return sb.toString();
    }
}