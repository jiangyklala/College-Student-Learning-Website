package com.jxm.yiti.resp;

public class UserQueryResp {

    private String username;

    private String useraccount;

    private String salt;

    private String githubId;

    private String email;

    private Long balance;

    private String others;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", username=").append(username);
        sb.append(", useraccount=").append(useraccount);
        sb.append(", salt=").append(salt);
        sb.append(", githubId=").append(githubId);
        sb.append(", email=").append(email);
        sb.append(", balance=").append(balance);
        sb.append(", others=").append(others);
        sb.append("]");
        return sb.toString();
    }
}