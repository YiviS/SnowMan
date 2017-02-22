package com.yivis.snowman.core.utils.base;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by XuGuang on 2017/2/22.
 * ID工具类
 */
@Service
@Lazy(false)
public class IdGen {

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
