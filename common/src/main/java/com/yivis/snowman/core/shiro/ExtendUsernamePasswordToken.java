package com.yivis.snowman.core.shiro;


import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created by XuGuang on 2017/2/16.
 * 用户和密码（包含验证码）令牌类
 */
public class ExtendUsernamePasswordToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;

    public ExtendUsernamePasswordToken() {
        super();
    }

    public ExtendUsernamePasswordToken(String username, char[] password,
                                       boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
