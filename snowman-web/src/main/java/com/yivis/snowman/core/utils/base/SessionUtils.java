package com.yivis.snowman.core.utils.base;

import com.yivis.snowman.core.config.Global;
import com.yivis.snowman.sys.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by XuGuang on 2017/3/12.
 */
public class SessionUtils {
    private static final String USER_KEY = "SESSION_USER";

    /**
     * 获取session用户信息
     *
     * @param request
     * @return
     */
    public final static SysUser getUser(HttpServletRequest request) {
        return getUser();
    }

    /**
     * 设置session用户信息
     *
     * @param request
     * @param user
     */
    public final static void setUser(HttpServletRequest request, SysUser user) {
        request.getSession().setAttribute(USER_KEY, user);
    }

    /**
     * 获取shiro用户信息
     *
     * @param request
     * @param user
     */
    public final static SysUser getUser() {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject) {
            return (SysUser) subject.getPrincipal();
        }
        return null;
    }

    /**
     * 获取用户编号
     */
    public final static String getUserId() {
        SysUser sysUser = getUser();
        return (sysUser == null) ? Global.ANONYMOUS_ID : sysUser.getId();
    }

    /**
     * 获取用户名
     */
    public final static String getUserName() {
        SysUser sysUser = getUser();
        return (sysUser == null) ? "" : sysUser.getUsername();
    }


    /**
     * 获取退出登录
     *
     * @param request
     * @param user
     */
    public final static void kickUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null && subject.isAuthenticated()) {
            subject.logout();
        }
    }
}
