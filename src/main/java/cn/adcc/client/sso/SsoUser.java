package cn.adcc.client.sso;

import java.io.Serializable;
import java.util.Map;

public class SsoUser implements Serializable {
    private static final long serialVersionUID = 42L;

    // field
    private String userid;
    private String username;
    private int roleId;
    private String permission;
    private Map<String, String> plugininfo;

    private String version;
    private int expireMinite;
    private long expireFreshTime;

    private int status;
    private int sensitiveLevel;



    // set get
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Map<String, String> getPlugininfo() {
        return plugininfo;
    }

    public void setPlugininfo(Map<String, String> plugininfo) {
        this.plugininfo = plugininfo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getExpireMinite() {
        return expireMinite;
    }

    public void setExpireMinite(int expireMinite) {
        this.expireMinite = expireMinite;
    }

    public long getExpireFreshTime() {
        return expireFreshTime;
    }

    public void setExpireFreshTime(long expireFreshTime) {
        this.expireFreshTime = expireFreshTime;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSensitiveLevel() {
        return sensitiveLevel;
    }

    public void setSensitiveLevel(int sensitiveLevel) {
        this.sensitiveLevel = sensitiveLevel;
    }
}
