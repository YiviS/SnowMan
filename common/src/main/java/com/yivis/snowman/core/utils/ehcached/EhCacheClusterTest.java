package com.yivis.snowman.core.utils.ehcached;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Statistics;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * cache cluster test class
 * 
 * @author Administrator
 * 
 */
public class EhCacheClusterTest {

    static CacheManager manager = CacheManager.getInstance();
    private static Logger logger = Logger.getLogger(EhCacheClusterTest.class);

    public static void showInfo() throws InterruptedException {
        DateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] cacheNames = manager.getCacheNames();
        logger.debug("读取的缓存列表为:");
        for (int i = 0; i < cacheNames.length; i++) {
            logger.debug("--" + (i + 1) + " " + cacheNames[i]);
        }

        // 数据字典缓存测试
        Cache cache = manager.getCache("CACHE_SYS_DATADIC");

        int elementSize = cache.getSize();
        logger.debug("得到缓存的对象数量：" + elementSize);

        // cache.remove("key1");
        // Element element1 = new Element("key1", "value1111111");
        // cache.put(element1);

        // 缓存元素集合
        logger.debug("-----------------------缓存元素统计数据---------------------------------");
        List keys = cache.getKeys();
        for (Object key : keys) {
            Element ele = cache.get(key);
            logger.debug("-------------KEY: " + ele.getKey() + "-------------");
            logger.debug("内容: " + ele.getValue());
            logger.debug("创建时间: " + sf.format(ele.getCreationTime()));
            logger.debug("最后访问时间: " + sf.format(ele.getLastAccessTime()));
            logger.debug("过期时间: " + sf.format(ele.getExpirationTime()));
            logger.debug("最后更新时间: " + sf.format(ele.getLastUpdateTime()));
            logger.debug("命中次数: " + ele.getHitCount());
            logger.debug("存活时间: " + ele.getTimeToLive() + "ms");
            logger.debug("空闲时间: " + ele.getTimeToIdle() + "ms");
        }
        logger.debug("--------------------------------------------------------");

        logger.debug("-----------------------缓存总统计数据---------------------------------");
        long elementsInMemory1 = cache.getMemoryStoreSize();
        logger.debug("得到缓存对象占用内存的数量：" + elementsInMemory1);

        long elementsInMemory2 = cache.getDiskStoreSize();
        logger.debug("得到缓存对对象占用磁盘的数量：" + elementsInMemory2);

        // 获取缓存统计对象
        Statistics stat = cache.getStatistics();
        long hits = stat.getCacheHits();
        logger.debug("得到缓存读取的命中次数：" + hits);

        long memoryHits = stat.getInMemoryHits();
        logger.debug("得到内存中缓存读取的命中次数：" + memoryHits);

        long diskHits = stat.getOnDiskHits();
        logger.debug("得到磁盘中缓存读取的命中次数：" + diskHits);

        long cacheMisses = stat.getCacheMisses();
        logger.debug("得到缓存读取的丢失次数：" + cacheMisses);

        long evictionCount = stat.getEvictionCount();
        logger.debug("得到缓存读取的已经被销毁的对象丢失次数：" + evictionCount);

        logger.debug("--------------------------------------------------------");
    }
}