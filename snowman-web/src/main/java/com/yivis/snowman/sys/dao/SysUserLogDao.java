package com.yivis.snowman.sys.dao;

import com.yivis.snowman.core.base.BaseDao;
import com.yivis.snowman.sys.entity.SysUserLog;
import com.yivis.snowman.sys.mapper.SysUserLogMapper;
import org.springframework.stereotype.Repository;

/**
 * Created by XuGuang on 2017/3/7.
 * 用户日志信息
 */
@SuppressWarnings("unchecked")
@Repository
public class SysUserLogDao extends BaseDao<SysUserLog, SysUserLogMapper, Integer> implements SysUserLogMapper {

    @Override
    public String getMapperNamesapce() {
        return SysUserLogMapper.class.getName().toString();
    }

}
