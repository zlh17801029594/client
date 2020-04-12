package cn.adcc.client.VO;

import java.util.List;

public class User {
    private List<String> roles;
    private String introduction;
    private String avatar;
    private String name;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(List<String> roles, String introduction, String avatar, String name) {
        this.roles = roles;
        this.introduction = introduction;
        this.avatar = avatar;
        this.name = name;
    }
}
