package com.yivis.snowman.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by XuGuang on 2017/2/16.
 */
public class SysUser implements Serializable {

    private String id;
    /**
     * 昵称
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 1:有效，0:禁止登录
     */
    private Long status;
    /**
     * 盐
     */
    private String salt;
    private Boolean locked = Boolean.FALSE;

    public SysUser() {
    }

    public SysUser(SysUser sysUser) {
        this.id = sysUser.getId();
        this.username = sysUser.getUsername();
        this.password = sysUser.getPassword();
        this.createTime = sysUser.getCreateTime();
        this.lastLoginTime = sysUser.getLastLoginTime();
        this.status = sysUser.getStatus();
        this.locked = sysUser.getLocked();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
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
