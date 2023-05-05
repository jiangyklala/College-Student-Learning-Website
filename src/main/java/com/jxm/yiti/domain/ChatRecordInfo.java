package com.jxm.yiti.domain;

public class ChatRecordInfo {
    private Long id;

    private String date;

    private String ntimes;

    private String ntokens;

    private String vtimes;

    private String vtokens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNtimes() {
        return ntimes;
    }

    public void setNtimes(String ntimes) {
        this.ntimes = ntimes;
    }

    public String getNtokens() {
        return ntokens;
    }

    public void setNtokens(String ntokens) {
        this.ntokens = ntokens;
    }

    public String getVtimes() {
        return vtimes;
    }

    public void setVtimes(String vtimes) {
        this.vtimes = vtimes;
    }

    public String getVtokens() {
        return vtokens;
    }

    public void setVtokens(String vtokens) {
        this.vtokens = vtokens;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", ntimes=").append(ntimes);
        sb.append(", ntokens=").append(ntokens);
        sb.append(", vtimes=").append(vtimes);
        sb.append(", vtokens=").append(vtokens);
        sb.append("]");
        return sb.toString();
    }
}