package com.yivis.snowman.core.shiro.filter;

import com.yivis.snowman.core.shiro.token.TokenManager;
import com.yivis.snowman.core.utils.base.LoggerUtils;
import com.yivis.snowman.core.utils.base.Servlets;
import com.yivis.snowman.sys.entity.SysUser;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XuGuang on 2017/3/2.
 * 判断是否登陆
 */
public class LoginFilter extends AccessControlFilter {

    @Autowired
    private TokenManager tokenManager;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        SysUser token = tokenManager.getToken();
        if (null != token || isLoginRequest(servletRequest, servletResponse)) {
            return Boolean.TRUE;
        }
        if (Servlets.isAjaxRequest((HttpServletRequest) servletRequest)) {
            Map<String, String> resultMap = new HashMap<String, String>();
            LoggerUtils.debug(getClass(), "当前用户没有登录，并且是Ajax请求！");
            resultMap.put("login_status", "300");
            resultMap.put("message", "当前用户没有登录");
            Servlets.out(servletResponse, resultMap);
        }
        return Boolean.FALSE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //保存Request和Response 到登录的链接
        saveRequestAndRedirectToLogin(servletRequest, servletResponse);
        return Boolean.FALSE;
    }
}
