package com.jxm.yiti.domain;

public class PracticeUser {
    private Long userId;

    private byte[] settingsObj;

    private byte[] doneIdList;

    private byte[] wrongIdList;

    private byte[] markIdList;

    private byte[] other;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public byte[] getSettingsObj() {
        return settingsObj;
    }

    public void setSettingsObj(byte[] settingsObj) {
        this.settingsObj = settingsObj;
    }

    public byte[] getDoneIdList() {
        return doneIdList;
    }

    public void setDoneIdList(byte[] doneIdList) {
        this.doneIdList = doneIdList;
    }

    public byte[] getWrongIdList() {
        return wrongIdList;
    }

    public void setWrongIdList(byte[] wrongIdList) {
        this.wrongIdList = wrongIdList;
    }

    public byte[] getMarkIdList() {
        return markIdList;
    }

    public void setMarkIdList(byte[] markIdList) {
        this.markIdList = markIdList;
    }

    public byte[] getOther() {
        return other;
    }

    public void setOther(byte[] other) {
        this.other = other;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", settingsObj=").append(settingsObj);
        sb.append(", doneIdList=").append(doneIdList);
        sb.append(", wrongIdList=").append(wrongIdList);
        sb.append(", markIdList=").append(markIdList);
        sb.append(", other=").append(other);
        sb.append("]");
        return sb.toString();
    }
}