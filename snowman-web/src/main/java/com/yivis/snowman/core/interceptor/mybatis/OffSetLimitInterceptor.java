package com.yivis.snowman.core.interceptor.mybatis;

import com.github.pagehelper.Dialect;
import com.yivis.snowman.core.utils.base.ReflectUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * Created by XuGuang on 2017/3/8.
 * mybatis 分页拦截器
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class OffSetLimitInterceptor implements Interceptor {

    //数据库类型，不同的数据库有不同的分页方法
    private String databaseType;
    private Dialect dialect;
    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;
    private static final int ROWBOUNDS_INDEX = 2;

    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 初始化Configuration设置属性
     */
    public void setProperties(Properties properties) {
        this.databaseType = properties.getProperty("databaseType");
    }

    public void processIntercept(Object[] queryArgs) {
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        BoundSql boundSql = ms.getBoundSql(parameter);
        String sql = boundSql.getSql();
        String newSql = this.getPageSql(sql, offset, limit);
        ReflectUtils.setFieldValue(rowBounds, "offset", RowBounds.NO_ROW_OFFSET);
        ReflectUtils.setFieldValue(rowBounds, "limit", RowBounds.NO_ROW_LIMIT);
        ReflectUtils.setFieldValue(boundSql, "sql", newSql);
        ReflectUtils.setFieldValue(ms, "sqlSource", new BoundSqlSqlSource(boundSql));
    }

    private String getPageSql(String sql, int offset, int limit) {
        StringBuffer sqlBuffer = new StringBuffer(sql);
        sqlBuffer.append(" limit ").append(limit).append(" offset ").append(offset);
        return sqlBuffer.toString();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
