package com.jxm.yiti.req;

public class UserQueryReq {
    private Long id;

    private String username;

    private String useraccount;

    private String password;

    private String githubId;

    private String email;

    private String salt;

    private Long balance;
    
    private String verifyCode;  // 邮箱验证码

    private String inviteCode;   // 邀请码

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", useraccount='" + useraccount + '\'' +
                ", password='" + password + '\'' +
                ", githubId='" + githubId + '\'' +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                ", balance=" + balance +
                ", verifyCode='" + verifyCode + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }
}
