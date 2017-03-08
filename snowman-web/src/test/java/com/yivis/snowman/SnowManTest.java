package com.yivis.snowman;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XuGuang on 2017/2/22.
 * 测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SnowManTest {

    private static Logger logger = LoggerFactory.getLogger(SnowManTest.class);

    @Autowired
    private CacheManager cacheManager;

    /**
     * 移除账号锁定缓存
     */
    @Test
    public void removeUser() {
        Cache<String, AtomicInteger> passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        passwordRetryCache.remove("super");
    }

    /**
     * rememberme cookie加密的密钥 建议每个项目都不一样 默认AES算法
     */
    @Test
    public void rememberEncode() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecretKey deskey = keygen.generateKey();
        System.out.println("==========" + Base64.encodeToString(deskey.getEncoded()));
    }

}
