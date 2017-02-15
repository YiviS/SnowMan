package com.yivis.snowman.core.utils.ehcached;

import net.sf.ehcache.Cache;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ehcache工具抽象类
 */
public abstract class AbstractEHCache<T> {
    protected Logger logger = Logger.getLogger(getClass());

    /**
     * 初始化数据
     * 
     * @throws BaseException
     */
    public abstract void initData() throws Exception;

    /**
     * 获得缓存名称
     */
    public abstract String getCacheName();

    /**
     * 获得缓存
     */
    public Cache getCache() {
        return EHCacheUtil.getCacheByName(getCacheName());
    }

    /**
     * 判断缓存数据是否为空
     */
    public final boolean checkData() {
        if (getCache().getSize() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断缓存是否为空 空则初始化
     * 
     * @throws BaseException
     */
    public void checkAndInitData() throws Exception {
        if (!checkData()) {
            initData();
        }
    }

    /**
     * 获取所有的缓存元素
     * 
     * @return
     * @throws BaseException
     */

    @SuppressWarnings("unchecked")
    public HashMap<String, T> getAllElements() throws Exception {
        checkAndInitData();
        return (HashMap<String, T>) EHCacheUtil.getCacheValues(getCacheName());
    }

    /**
     * 获取所有的缓存元素(返回list)
     * 
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> getAllElementsList() throws Exception {
        checkAndInitData();
        return (ArrayList<T>) EHCacheUtil.getCacheValuesList(getCacheName());
    }

    /**
     * 根据key获取元素缓存值
     * 
     * @return
     * @throws BaseException
     */
    @SuppressWarnings("unchecked")
    public T getElementByKey(String key) throws Exception {
        checkAndInitData();
        return (T) EHCacheUtil.getValue(getCacheName(), key);
    }

    /**
     * 根据key设置元素缓存值
     * 
     * @throws BaseException
     */
    public void setElementByKey(String key, T object) throws Exception {
        // 实现集群需要先remove再put
        /* EHCacheUtil.remove(getCacheName(), key); */
        EHCacheUtil.setValue(getCacheName(), key, object);
    }
}
