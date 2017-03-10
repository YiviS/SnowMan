package com.yivis.snowman.core.utils.dynamicDataSource;

/**
 * Created by XuGuang on 2017/3/10.
 */
public class DataSourceHolder {
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    /**
     * 设置当前数据库。
     *
     * @param dataSource
     */
    public static void setDataSources(String dataSource) {
        dataSources.set(dataSource);
    }

    /**
     * 取得当前数据源。
     *
     * @return
     */
    public static String getDataSources() {
        return dataSources.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        dataSources.remove();
    }
}
