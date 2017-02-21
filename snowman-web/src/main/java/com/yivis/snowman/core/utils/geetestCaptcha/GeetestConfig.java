package com.yivis.snowman.core.utils.geetestCaptcha;

/**
 * Created by XuGuang on 2017/2/21.
 * GeetestWeb配置文件
 */
public class GeetestConfig {
    // 填入自己的captcha_id和private_key
    private static final String geetest_id = "b1ded257901af6fc250bf9901c9c54aa";
    private static final String geetest_key = "20c49114cf727371e79f4434f356db5f";

    public static final String getGeetest_id() {
        return geetest_id;
    }

    public static final String getGeetest_key() {
        return geetest_key;
    }
}
