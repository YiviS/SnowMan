package com.yivis.snowman.core.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by XuGuang on 2017/2/21.
 * 验证码处理类
 */
public class CaptchaException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public CaptchaException() {
        super();
    }

    public CaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(Throwable cause) {
        super(cause);
    }
}
