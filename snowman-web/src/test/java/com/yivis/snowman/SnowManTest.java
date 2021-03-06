package com.yivis.snowman;

import com.yivis.snowman.core.utils.base.IdGen;
import com.yivis.snowman.core.utils.dynamicDataSource.DataSourceHolder;
import com.yivis.snowman.sys.entity.SysUser;
import com.yivis.snowman.sys.service.SysUserLogService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private SysUserLogService sysUserLogService;

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

    @Test
    public void DynamicDataSource(){
//        DataSourceHolder.setDataSources("dataSource2");
        List<Map> list = sysUserLogService.getMapList();
//        List list = sysUserLogService.getList(new SysUserLog());
        System.out.println(DataSourceHolder.getDataSources() +"=========="+ list.size());
    }
    @Test
    public void testID(){
        System.out.println(Integer.valueOf(IdGen.uuid()));
    }

    @Test
    public void testListBen(){
        SysUser sysUser = new SysUser();
        List<SysUser> list = new ArrayList<SysUser>();
        list.add(sysUser);
        Iterator it1 = list.iterator();
        while (it1.hasNext()){
            Field[] fields = it1.next().getClass().getDeclaredFields();
            for(int i=0;i<fields.length;i++){
                System.out.println("======"+fields[i].getName().toString());
            }
        }
    }

}
