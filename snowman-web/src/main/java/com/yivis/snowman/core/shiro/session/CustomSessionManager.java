package com.yivis.snowman.core.shiro.session;

import com.yivis.snowman.core.config.SysConfig;
import com.yivis.snowman.core.utils.base.LoggerUtils;
import com.yivis.snowman.core.utils.base.StringUtils;
import com.yivis.snowman.sys.entity.SysUser;
import com.yivis.snowman.sys.entity.SysUserOnlineBo;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
     * 根据ID查询 SimplePrincipalCollection
     *
     * @param userIds 用户ID
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<SimplePrincipalCollection> getSimplePrincipalCollectionByUserId(Long... userIds) {
        //把userIds 转成Set，好判断
        Set<Long> idset = (Set<Long>) StringUtils.array2Set(userIds);
        //获取所有session
        Collection<Session> sessions = mysessionDao.getActiveSessions();
        //定义返回
        List<SimplePrincipalCollection> list = new ArrayList<SimplePrincipalCollection>();
        for (Session session : sessions) {
            //获取SimplePrincipalCollection
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (null != obj && obj instanceof SimplePrincipalCollection) {
                //强转
                SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
                //判断用户，匹配用户ID。
                obj = spc.getPrimaryPrincipal();
                if (null != obj && obj instanceof SysUser) {
                    SysUser user = (SysUser) obj;
                    //比较用户ID，符合即加入集合
                    if (null != user && idset.contains(user.getId())) {
                        list.add(spc);
                    }
                }
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
                String onlineStatus = (String) session.getAttribute(SysConfig.ONLINE_STATUS);
                userBo.setOnlineStatus(onlineStatus);
                return userBo;
            }
        }
        return null;
    }

    /**
     * 改变Session状态
     *
     * @param status    online("在线"),locked("锁定"), hidden("隐身"), kickout("踢出");
     * @param sessionId
     * @return
     */
    public Map<String, String> changeSessionStatus(String onlineStatus, String sessionIds) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] sessionIdArray = null;
            if (sessionIds.indexOf(",") == -1) {
                sessionIdArray = new String[]{sessionIds};
            } else {
                sessionIdArray = sessionIds.split(",");
            }
            for (String id : sessionIdArray) {
                Session session = mysessionDao.readSession(id);
                session.setAttribute(SysConfig.ONLINE_STATUS, onlineStatus);
                mysessionDao.update(session);
            }
            map.put("onlineStatus", onlineStatus);
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(), e, "改变Session状态错误，sessionId[%s]", sessionIds);
            map.put("onlineStatus", "500");
            map.put("message", "改变失败，有可能Session不存在，请刷新再试！");
        }
        return map;
    }

}
