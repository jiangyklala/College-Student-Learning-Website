package com.jxm.yiti.req;

public class UserQueryReq {
    private Long id;

    private String username;

    private String useraccount;

    private String password;

    private String salt;

    private String githubId;

    private String email;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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
                ", salt='" + salt + '\'' +
                ", githubId='" + githubId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
