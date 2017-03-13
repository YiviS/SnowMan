package com.yivis.snowman.core.aop.log;

import com.alibaba.fastjson.JSONObject;
import com.yivis.snowman.sys.entity.SysUserLog;
import com.yivis.snowman.sys.service.SysUserLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class UserLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserLogAspect.class);
    @Autowired
    private SysUserLogService sysUserLogService;

    @After(value = "@annotation(userLog)")
    public void saveUserLog(JoinPoint joinPoit, UserLog userLog) throws Exception {
        SysUserLog sysUserLog = new SysUserLog();
        for (Object object : joinPoit.getArgs()) {
            if (object.getClass().equals(userLog.remarkClass())) {
                sysUserLog.setRemark(JSONObject.toJSONString(object) + "|" + sysUserLog.getRemark());
            }
            if (object instanceof HttpServletRequest) {
                sysUserLog.setOperIP(getAddr((HttpServletRequest) object));
            }
        }
        sysUserLog.setCrtTime(new Date());
        sysUserLog.setOperCode(userLog.code());
        sysUserLog.setOperName(userLog.name());
        sysUserLog.setLogType(userLog.type());
        sysUserLogService.saveSysUserLog(sysUserLog);
    }


    private String getAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }

}
