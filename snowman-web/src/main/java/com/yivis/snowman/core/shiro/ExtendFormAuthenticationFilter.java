package com.yivis.snowman.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by XuGuang on 2017/2/17.
 * 表单验证（包含验证码）过滤类
 */
public class ExtendFormAuthenticationFilter extends FormAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    /**
     * 所有请求都会经过的方法。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //可自己扩展此方法，默认父类中的方法
        return super.onAccessDenied(request, response);
    }

    /**
     * 创建token
     */
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request) == null ? "" : getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String host = request.getRemoteHost();
        String captcha = getCaptcha(request);
        return new ExtendUsernamePasswordToken(username, password.toCharArray(), rememberMe, host);
    }

    /**
     * 重写执行登录方法
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        String username = new String(getUsername(request));
        String password = new String(getPassword(request));
        if (logger.isDebugEnabled()) {
            logger.debug("------- 权限验证, active session username: {}, password: {}", username, password);
        }
        //可自己扩展此方法，默认父类中的方法
        return super.executeLogin(request, response);
    }

    /**
     * 登入成功的处理方法
     */
    @Override
    public boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                  ServletResponse response) throws Exception {
        //TODO 记录登陆日志

        if (!isAjax(request)) {     // ------------ 非ajax请求 ------------
            return super.onLoginSuccess(token, subject, request, response);
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("{\"success\":true,\"message\":\"登录成功\"}");
            out.flush();
            out.close();
        }
        return false;
    }

    /**
     * 登入失败的方法
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        //TODO 记录登陆日志

        // ------------ 非ajax请求 ------------
        if (!isAjax(request)) {
            String className = e.getClass().getName();
            String message = getFailureKeyAttribute();
            request.setAttribute(message, className);
            setFailureAttribute(request, e);
            return true;
        }
        // ------------ ajax请求 ------------
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=utf-8");
            PrintWriter out = response.getWriter();
            String message = e.getClass().getSimpleName();
            if ("IncorrectCredentialsException".equals(message)) {
                out.println("{\"success\":false,\"message\":\"密码错误\"}");
            } else if ("UnknownAccountException".equals(message)) {
                out.println("{\"success\":false,\"message\":\"账号不存在\"}");
            } else if ("LockedAccountException".equals(message)) {
                out.println("{\"success\":false,\"message\":\"账号被锁定\"}");
            } else {
                out.println("{\"success\":false,message:\"未知错误\"}");
            }
            out.flush();
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * 获取验证码
     */
    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, DEFAULT_CAPTCHA_PARAM);
    }

    /**
     * 覆盖父类方法、解决所有失败的认证都跳转到登录页面
     */
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
    }

    /**
     * 判断是不是ajax请求
     */
    private boolean isAjax(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return "XMLHttpRequest".equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
    }
}
