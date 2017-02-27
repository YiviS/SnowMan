package com.yivis.snowman.sys.entity;

import java.io.Serializable;

/**
 * Created by XuGuang on 2017/2/16.
 */
public class SysUser implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String salt;
    private Boolean locked = Boolean.FALSE;

    public SysUser(){}

    public SysUser(SysUser sysUser) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.locked = sysUser.getLocked();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getCredentialsSalt() {
        return username + salt;
    }
}
