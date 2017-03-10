package com.yivis.snowman.sys.mapper;

import com.yivis.snowman.core.base.BaseSqlMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by XuGuang on 2017/3/7.
 */
public interface SysUserLogMapper extends BaseSqlMapper {

    @Select("SELECT * FROM SYS_USER_LOG")
    List<Map> getMapList();
}
