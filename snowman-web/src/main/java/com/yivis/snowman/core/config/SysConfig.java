package com.yivis.snowman.core.config;

/**
 * Created by XuGuang on 2017/3/1.
 * 项目属性
 */
public class SysConfig {

    public static SysConfig sysConfig = new SysConfig();

    /**
     * 验证码开关 false（关） true（开）
     */
    public final static boolean CAPTCHA_SWITCH = true;
    /**
     * 用户状态：online（在线） kickout（踢出）locked（锁定）
     */
    public final static String ONLINE_STATUS = "online_status";

    public static enum OnlineStatus {
        online("在线"), locked("锁定"), hidden("隐身"), kickout("踢出");
        private final String info;

        private OnlineStatus(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }
}
