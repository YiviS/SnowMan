package com.yivis.snowman.sys.service;

import com.yivis.snowman.sys.dao.SysUserLogDao;
import com.yivis.snowman.sys.entity.SysUserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by XuGuang on 2017/3/7.
 * 用户日志
 */
@Service
public class SysUserLogService {
    @Autowired
    private SysUserLogDao sysUserLogDao;

    public List<SysUserLog> getList(SysUserLog bo) {
        return sysUserLogDao.pageQuery(bo, 1, 2);
    }

    public List<Map> getMapList() {
        return sysUserLogDao.getMapList();
    }
}
