package com.jxm.yiti.resp;

public class WxUserInfoResp {

    private Integer points;

    private String other1;

    private Integer other2;

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

    public Integer getOther2() {
        return other2;
    }

    public void setOther2(Integer other2) {
        this.other2 = other2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", points=").append(points);
        sb.append(", other1=").append(other1);
        sb.append(", other2=").append(other2);
        sb.append("]");
        return sb.toString();
    }
}