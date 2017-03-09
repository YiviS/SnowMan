package com.yivis.snowman.core.base;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by XuGuang on 2017/3/7.
 * 基础Dao
 */
public abstract class BaseDao<E, M, P extends Serializable> extends SqlSessionDaoSupport {

    protected static final Logger logger = LoggerFactory.getLogger(BaseDao.class);

    @Override
    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * 获取list
     *
     * @param entity
     * @return
     */
    public List<E> getList(E entity) {
        List<E> list = getSqlSession().selectList(getMapperNamesapce() + ".getList", entity);
        return list;
    }

    public List<E> pageQuery(E entity, int offet, int limit) {
        List<E> list = getSqlSession().selectList(getMapperNamesapce() + ".getList", entity, new RowBounds(offet, limit));
        return list;
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    public int save(E entity) {
        return getSqlSession().insert(getMapperNamesapce() + ".insert", entity);
    }

    /**
     * 修改主键修改
     *
     * @param entity
     */
    public int update(E entity) {
        int affectCount = getSqlSession().update(getMapperNamesapce() + ".update", entity);
        return affectCount;
    }

    /**
     * 获取Mapper命名空间 用于被子类覆盖
     *
     * @return
     */
    public String getMapperNamesapce() {
        throw new RuntimeException("not yet implement");
    }
}
