package com.yivis.snowman.core.utils.ehcached;

/**
 * @className:EHCacheConfig.java
 * @classDescription:
 */
public class EHCacheConfig {
    /**
     * 元素最大数量
     */
    public static final int MAXELEMENTSINMEMORY = 50000;
    /**
     * 是否把溢出数据持久化到硬盘
     */
    public static final boolean OVERFLOWTODISK = true;
    /**
     * 是否会死亡
     */
    public static final boolean ETERNAL = false;
    /**
     * 缓存的间隔是时间
     */
    public static final int TIMETOIDLESECONDS = 300;
    /**
     *
     * 存活时间(默认一天)
     */
    public static int TIMETOlIVESECONDS = 86400;
    /**
     * 需要持久化到硬盘否
     */
    public static final boolean DISKPERSISTENT = false;
    /**
     * 内存存取策略
     */
    public static final String MEMORYSTOREEVICTIONPOLICY = "LFU";

}
