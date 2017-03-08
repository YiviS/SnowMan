package com.yivis.snowman.core.interceptor.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * Created by XuGuang on 2017/3/8.
 * mybatis 分页拦截器
 */
@Intercepts(@Signature(method = "query", type = Executor.class,
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class OffSetLimitInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        System.out.println("Invocation.proceed()");
        return result;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String prop1 = properties.getProperty("prop1");
        String prop2 = properties.getProperty("prop2");
        System.out.println(prop1 + "------" + prop2);
    }
}
