package com.yivis.snowman.sys.service;

import com.yivis.snowman.core.base.BaseService;
import com.yivis.snowman.core.utils.base.IdGen;
import com.yivis.snowman.core.utils.base.SessionUtils;
import com.yivis.snowman.sys.dao.SysUserLogDao;
import com.yivis.snowman.sys.entity.SysUserLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by XuGuang on 2017/3/7.
 * 用户日志
 */
@Service
public class SysUserLogService extends BaseService {
    @Autowired
    private SysUserLogDao sysUserLogDao;

    public List<SysUserLog> getList(SysUserLog bo) {
        return sysUserLogDao.pageQuery(bo, 1, 2);
    }

    public List<Map> getMapList() {
        return sysUserLogDao.getMapList();
    }

    /**
     * 保存用于日志信息
     */
    public void saveSysUserLog(SysUserLog sysUserLog) {
        String idStr = IdGen.uuid();
        sysUserLog.setLogId(idStr);
        logger.debug("---------------------------------logid:>" + sysUserLog.getLogId());
        sysUserLog.setUserId(SessionUtils.getUserId() == "0" ? null : SessionUtils.getUserId());
        sysUserLog.setUsername(SessionUtils.getUserName());
        sysUserLogDao.save(sysUserLog);
    }
}
