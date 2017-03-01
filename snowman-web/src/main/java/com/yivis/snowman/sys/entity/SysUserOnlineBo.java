package com.yivis.snowman.sys.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by XuGuang on 2017/2/27.
 * Session  + User Bo
 */
public class SysUserOnlineBo extends SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    //Session Id
    private String sessionId;
    //Session Host
    private String host;
    //Session创建时间
    private Date startTime;
    //Session最后交互时间
    private Date lastAccess;
    //Session timeout
    private long timeout;
    //用户状态
    private String onlineStatus;

    public SysUserOnlineBo(SysUser user) {
        super(user);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
