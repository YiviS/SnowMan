package com.yivis.snowman.core.interceptor.mybatis;

import com.yivis.snowman.core.utils.jdbc.dialect.Dialect;
import com.yivis.snowman.core.utils.jdbc.dialect.MySQLDialect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by XuGuang on 2017/3/8.
 * mybatis 分页拦截器
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class OffSetLimitInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(OffSetLimitInterceptor.class);

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

        String dialectClass = properties.getProperty("dialectClass");
        try {
//            this.dialect = (Dialect) Class.forName(dialectClass).newInstance();
            this.dialect = new MySQLDialect();
        } catch (Exception e) {
            logger.error("cannot create dialect instance by dialectClass:" + dialectClass, e);
        }
        logger.debug(OffSetLimitInterceptor.class.getSimpleName() + ".dialect=" + dialectClass);
    }

    /**
     * 拦截器执行的方法
     */
    public void processIntercept(final Object[] queryArgs) {
        MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        if (dialect.supportsLimit() && (offset != RowBounds.NO_ROW_OFFSET || limit != RowBounds.NO_ROW_LIMIT)) {
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();
            if (dialect.supportsLimitOffset()) {
                sql = dialect.getLimitString(sql, offset, limit);
                offset = RowBounds.NO_ROW_OFFSET;
            } else {
                sql = dialect.getLimitString(sql, 0, limit);
            }
            limit = RowBounds.NO_ROW_LIMIT;

            queryArgs[ROWBOUNDS_INDEX] = new RowBounds(offset, limit);

            BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);

            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
        }
    }

    /**
     * 构造新的 BoundSql
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * 构造新的 MappedStatement
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {

        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        /*
         * 不兼容mybatis3.1 拿掉 builder.keyProperty(ms.getKeyProperty());
         */

        // setStatementTimeout()
        builder.timeout(ms.getTimeout());

        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
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
