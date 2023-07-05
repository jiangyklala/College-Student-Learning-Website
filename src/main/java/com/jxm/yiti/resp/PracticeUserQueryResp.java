package com.jxm.yiti.resp;

import com.jxm.yiti.domain.cust.PracticeSettings;

import java.util.List;

public class PracticeUserQueryResp {
    private PracticeSettings settingsObj;

    private List<Long> doneIdList;

    private List<Long> wrongIdList;

    private List<Long> markIdList;

    private List<Long> other;

    public PracticeSettings getSettingsObj() {
        return settingsObj;
    }

    public void setSettingsObj(PracticeSettings settingsObj) {
        this.settingsObj = settingsObj;
    }

    public List<Long> getDoneIdList() {
        return doneIdList;
    }

    public void setDoneIdList(List<Long> doneIdList) {
        this.doneIdList = doneIdList;
    }

    public List<Long> getWrongIdList() {
        return wrongIdList;
    }

    public void setWrongIdList(List<Long> wrongIdList) {
        this.wrongIdList = wrongIdList;
    }

    public List<Long> getMarkIdList() {
        return markIdList;
    }

    public void setMarkIdList(List<Long> markIdList) {
        this.markIdList = markIdList;
    }

    public List<Long> getOther() {
        return other;
    }

    public void setOther(List<Long> other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "PracticeUserQueryResp{" +
                "settingsObj=" + settingsObj +
                ", doneIdList=" + doneIdList +
                ", wrongIdList=" + wrongIdList +
                ", markIdList=" + markIdList +
                ", other=" + other +
                '}';
    }
}