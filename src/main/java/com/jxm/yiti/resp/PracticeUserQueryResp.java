package com.jxm.yiti.resp;

import com.jxm.yiti.domain.cust.PracticeSettings;

import java.util.List;

public class PracticeUserQueryResp {
    private PracticeSettings settingsObj;

    private List<Integer> doneIdList;

    private List<Integer> wrongIdList;

    private List<Integer> markIdList;

    private List<Integer> other;

    public PracticeSettings getSettingsObj() {
        return settingsObj;
    }

    public void setSettingsObj(PracticeSettings settingsObj) {
        this.settingsObj = settingsObj;
    }

    public List<Integer> getDoneIdList() {
        return doneIdList;
    }

    public void setDoneIdList(List<Integer> doneIdList) {
        this.doneIdList = doneIdList;
    }

    public List<Integer> getWrongIdList() {
        return wrongIdList;
    }

    public void setWrongIdList(List<Integer> wrongIdList) {
        this.wrongIdList = wrongIdList;
    }

    public List<Integer> getMarkIdList() {
        return markIdList;
    }

    public void setMarkIdList(List<Integer> markIdList) {
        this.markIdList = markIdList;
    }

    public List<Integer> getOther() {
        return other;
    }

    public void setOther(List<Integer> other) {
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