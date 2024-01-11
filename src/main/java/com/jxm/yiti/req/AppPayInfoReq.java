package com.jxm.yiti.req;

public class AppPayInfoReq {

    private String resultCode;

    private String transactionId;

    private String appId;

    private String openId;

    private Integer totalFee;

    private String outTradeNo;

    private String subMchId;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", resultCode=").append(resultCode);
        sb.append(", transactionId=").append(transactionId);
        sb.append(", appId=").append(appId);
        sb.append(", openId=").append(openId);
        sb.append(", totalFee=").append(totalFee);
        sb.append(", outTradeNo=").append(outTradeNo);
        sb.append(", subMchId=").append(subMchId);
        sb.append("]");
        return sb.toString();
    }
}