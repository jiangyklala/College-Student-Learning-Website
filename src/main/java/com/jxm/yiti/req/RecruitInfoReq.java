package com.jxm.yiti.req;

import java.util.Arrays;

public class RecruitInfoReq {

    private Integer id;

    private String company;

    private String recruitmentTarget;

    private String startDate;

    private String distanceFromStartDate;

    private String endDate;

    private String distanceFromEndDate;

    private String cityNature;

    private String deliverAddress;

    private String extrapolation;

    private String importantEvents;

    private byte[] city;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRecruitmentTarget() {
        return recruitmentTarget;
    }

    public void setRecruitmentTarget(String recruitmentTarget) {
        this.recruitmentTarget = recruitmentTarget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDistanceFromStartDate() {
        return distanceFromStartDate;
    }

    public void setDistanceFromStartDate(String distanceFromStartDate) {
        this.distanceFromStartDate = distanceFromStartDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDistanceFromEndDate() {
        return distanceFromEndDate;
    }

    public void setDistanceFromEndDate(String distanceFromEndDate) {
        this.distanceFromEndDate = distanceFromEndDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityNature() {
        return cityNature;
    }

    public void setCityNature(String cityNature) {
        this.cityNature = cityNature;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getExtrapolation() {
        return extrapolation;
    }

    public void setExtrapolation(String extrapolation) {
        this.extrapolation = extrapolation;
    }

    public String getImportantEvents() {
        return importantEvents;
    }

    public void setImportantEvents(String importantEvents) {
        this.importantEvents = importantEvents;
    }

    public byte[] getCity() {
        return city;
    }

    public void setCity(byte[] city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "RecruitInfoReq{" +
                "id=" + id +
                ", company='" + company + '\'' +
                ", recruitmentTarget='" + recruitmentTarget + '\'' +
                ", startDate='" + startDate + '\'' +
                ", distanceFromStartDate='" + distanceFromStartDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", distanceFromEndDate='" + distanceFromEndDate + '\'' +
                ", cityNature='" + cityNature + '\'' +
                ", deliverAddress='" + deliverAddress + '\'' +
                ", extrapolation='" + extrapolation + '\'' +
                ", importantEvents='" + importantEvents + '\'' +
                ", city=" + Arrays.toString(city) +
                '}';
    }
}