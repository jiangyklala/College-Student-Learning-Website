package com.jxm.yiti.resp;

public class UserQueryResp {

    private Long id;

    private String username;

    private String useraccount;

    private String githubId;

    private String email;

    private Long balance;

    private Integer type;

    private String remainDays;

    public String getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(String remainDays) {
        this.remainDays = remainDays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserQueryResp{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", useraccount='" + useraccount + '\'' +
                ", githubId='" + githubId + '\'' +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                ", type=" + type +
                ", remainDays='" + remainDays + '\'' +
                '}';
    }
}