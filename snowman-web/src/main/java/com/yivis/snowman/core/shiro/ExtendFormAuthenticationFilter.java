package com.yivis.snowman.core.shiro;

import com.yivis.snowman.core.utils.geetestCaptcha.GetGeetestCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by XuGuang on 2017/2/17.
 * 表单验证（包含验证码）过滤类
 */
public class ExtendFormAuthenticationFilter extends FormAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GetGeetestCaptcha getGeetestCaptcha;

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
        //验证验证码
        boolean captcha = issueCaptcha(request, response);
        if (!captcha) {
            request.setAttribute("message", "验证码错误");
        }
        return new ExtendUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
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
            String message = "";
            if (IncorrectCredentialsException.class.getName().equals(className)
                    || UnknownAccountException.class.getName().equals(className)) {
                message = "用户或密码错误, 请重试！";
            } else if (ExcessiveAttemptsException.class.getName().equals(className)) {
                message = "尝试次数过多，账号锁定！";
            } else if (StringUtils.isNotEmpty(e.getMessage())) {
                message = e.getMessage();
            } else {
                message = "系统出现点问题，请稍后再试！";
                e.printStackTrace(); // 输出到控制台
            }
            request.setAttribute(getFailureKeyAttribute(), className);
            request.setAttribute("message", message);
            return true;
        }
        // ------------ ajax请求 ------------
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain;charset=utf-8");
            PrintWriter out = response.getWriter();
            String message = e.getClass().getSimpleName();
            if ("CaptchaException".equals(message)) {
                out.println("{\"success\":false,\"message\":\"验证码错误\"}");
            } else if ("IncorrectCredentialsException".equals(message)) {
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
     * 验证验证码是否正确
     */
    protected boolean issueCaptcha(ServletRequest request, ServletResponse response) {
        return getGeetestCaptcha.isGeestesCaptcha((HttpServletRequest) request, (HttpServletResponse) response);
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
