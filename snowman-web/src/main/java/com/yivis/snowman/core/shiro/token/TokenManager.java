package com.yivis.snowman.core.shiro.token;

import com.yivis.snowman.core.shiro.exception.CaptchaException;
import com.yivis.snowman.core.utils.geetestCaptcha.GeetestBo;
import com.yivis.snowman.core.utils.geetestCaptcha.GetGeetestCaptcha;
import com.yivis.snowman.sys.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XuGuang on 2017/3/2.
 * Shiro管理下的Token工具类
 */
@Service
public class TokenManager {

    @Autowired
    private GetGeetestCaptcha getGeetestCaptcha;

    /**
     * 获取当前登录的用户User对象
     */
    public static SysUser getToken() {
        SysUser token = (SysUser) SecurityUtils.getSubject().getPrincipal();
        return token;
    }

    /**
     * 创建token
     */
    public AuthenticationToken createToken(SysUser sysUser, GeetestBo geetestBo, Boolean rememberMe, ServletRequest request, ServletResponse response) {
        String username = sysUser.getUsername();
        String password = sysUser.getPassword();
        String host = request.getRemoteHost();
        //验证验证码
        boolean captcha = issueCaptcha(request, geetestBo);
        if (!captcha) {
            request.setAttribute("message", "验证码错误");
        }
        return new ExtendUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
    }

    /**
     * 获取当前用户的Session
     *
     * @return
     */
    public Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 获取当前用户NAME
     *
     * @return
     */
    public String getUsername() {
        return getToken().getUsername();
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    public String getUserId() {
        return getToken() == null ? null : getToken().getId();
    }

    /**
     * 把值放入到当前登录用户的Session里
     *
     * @param key
     * @param value
     */
    public void setVal2Session(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    /**
     * 从当前登录用户的Session里取值
     *
     * @param key
     * @return
     */
    public Object getVal2Session(Object key) {
        return getSession().getAttribute(key);
    }

    /**
     * 登录 拦截登陆失败的错误
     *
     * @param user
     * @param rememberMe
     * @return
     */
    public Map<String, String> login(AuthenticationToken token) {
        Map<String, String> map = new HashMap<String, String>();
        String className = "";
        String message = "";
        String status = "200";
        try {
            SecurityUtils.getSubject().login(token);
            SysUser sysUser = this.getToken();
        } catch (AuthenticationException e) {
            className = e.getClass().getName();
            status = "500";
            if (IncorrectCredentialsException.class.getName().equals(className)
                    || UnknownAccountException.class.getName().equals(className)) {
                message = "用户或密码错误, 请重试！";
            } else if (CaptchaException.class.getName().equals(className)) {
                message = "验证码错误, 请重试！";
            } else if (ExcessiveAttemptsException.class.getName().equals(className)) {
                message = "尝试次数过多，账号锁定！";
            } else if (StringUtils.isNotEmpty(e.getMessage())) {
                message = e.getMessage();
            } else {
                message = "系统出现点问题，请稍后再试！";
                e.printStackTrace(); // 输出到控制台
            }
        }
        map.put("message", message);
        map.put("status", status);
        return map;
    }

    /**
     * 验证验证码是否正确
     */
    protected boolean issueCaptcha(ServletRequest request, GeetestBo geetestBo) {
        return getGeetestCaptcha.isGeestesCaptcha((HttpServletRequest) request, geetestBo);
    }
}
