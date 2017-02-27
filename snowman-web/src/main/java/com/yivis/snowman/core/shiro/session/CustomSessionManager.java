package com.yivis.snowman.core.shiro.session;

import com.yivis.snowman.sys.entity.SysUser;
import com.yivis.snowman.sys.entity.SysUserOnlineBo;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by XuGuang on 2017/2/27.
 * 用户Session 手动管理
 */
@Component
public class CustomSessionManager {

    /**
     * session status
     */
    public static final String SESSION_STATUS = "sojson-online-status";

    @Autowired
    public MySessionDao mysessionDao;

    @Autowired
    public EhCacheManager cacheManager;

    /**
     * 获取所有的有效Session用户
     *
     * @return
     */
    public List<SysUserOnlineBo> getAllUser() {
        //获取所有session
        Collection<Session> sessions = mysessionDao.getActiveSessions();
        List<SysUserOnlineBo> list = new ArrayList<SysUserOnlineBo>();

        for (Session session : sessions) {
            SysUserOnlineBo bo = getSessionBo(session);
            if (null != bo) {
                list.add(bo);
            }
        }
        return list;
    }

    /**
     * 获取单个Session
     *
     * @param sessionId
     * @return
     */
    public SysUserOnlineBo getSession(String sessionId) {
        Session session = mysessionDao.readSession(sessionId);
        SysUserOnlineBo bo = getSessionBo(session);
        return bo;
    }

    /**
     * session 转换为 SysUserOnlineBo
     */
    public SysUserOnlineBo getSessionBo(Session session) {
        //获取session登录信息。
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (null == obj) {
            return null;
        }
        //确保是 SimplePrincipalCollection对象。
        if (obj instanceof SimplePrincipalCollection) {
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            /**
             * 获取用户登录的，@link SampleRealm.doGetAuthenticationInfo(...)方法中
             * return new SimpleAuthenticationInfo(user,user.getPswd(), getName());的user 对象。
             */
            obj = spc.getPrimaryPrincipal();
            if (null != obj && obj instanceof SysUser) {
                //存储session + user 综合信息
                SysUserOnlineBo userBo = new SysUserOnlineBo((SysUser) obj);
                //最后一次和系统交互的时间
                userBo.setLastAccess(session.getLastAccessTime());
                //主机的ip地址
                userBo.setHost(session.getHost());
                //session ID
                userBo.setSessionId(session.getId().toString());
                //session最后一次与系统交互的时间
//                userBo.setLastLoginTime(session.getLastAccessTime());
                //回话到期 ttl(ms)
                userBo.setTimeout(session.getTimeout());
                //session创建时间
                userBo.setStartTime(session.getStartTimestamp());
                //是否踢出
                SessionStatus sessionStatus = (SessionStatus) session.getAttribute(SESSION_STATUS);
                boolean status = Boolean.TRUE;
                if (null != sessionStatus) {
                    status = sessionStatus.getOnlineStatus();
                }
                userBo.setSessionStatus(status);
                return userBo;
            }
        }
        return null;
    }

}
