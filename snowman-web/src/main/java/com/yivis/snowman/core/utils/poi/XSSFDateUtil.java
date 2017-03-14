package com.yivis.snowman.core.utils.poi;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;

/**
 * Created by XuGuang on 2017/3/14.
 * 自定义xssf日期工具类
 */
public class XSSFDateUtil extends DateUtil {
    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return DateUtil.absoluteDay(cal, use1904windowing);
    }
}
