package com.yivis.snowman.sys.entity;

import java.util.Date;

/**
 * Created by XuGuang on 2017/3/7.
 * 用户日志表 entity层
 */
public class SysUserLog {

    // 日志ID
    private String logId;
    // 类型(1:业务日志;0:操作日志)
    private Integer logType;
    // 用户ID
    private String userId;
    // 操作编码
    private String operCode;
    // 操作名称
    private String operName;
    // 操作时的IP
    private String operIP;
    // 备注
    private String remark = "";
    // 创建时间
    private Date crtTime;
    // 页面显示时间
    private String crtTimeStr;
    // 页面显示类型
    private String logTypeStr;
    // 用户登陆名称
    private String username;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getOperIP() {
        return operIP;
    }

    public void setOperIP(String operIP) {
        this.operIP = operIP;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    public String getCrtTimeStr() {
        return crtTimeStr;
    }

    public void setCrtTimeStr(String crtTimeStr) {
        this.crtTimeStr = crtTimeStr;
    }

    public String getLogTypeStr() {
        return logTypeStr;
    }

    public void setLogTypeStr(String logTypeStr) {
        this.logTypeStr = logTypeStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
