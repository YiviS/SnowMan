package com.yivis.snowman.core.shiro.filter;

import com.yivis.snowman.core.config.SysConfig;
import com.yivis.snowman.core.utils.base.StringUtils;
import com.yivis.snowman.sys.entity.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by XuGuang on 2017/2/28.
 * 相同帐号登录控制
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl; //踢出后到的地址
    //在线用户
    final static String ONLINE_USER = "online_user";
    //踢出状态，true标示踢出
    final static String KICKOUT_STATUS = "kickout_status";
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;

    }

    /**
     * 判断是不是登陆过，如果登陆过踢出前者（kickoutAfter = false）
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        //如果是相关目录 or 如果没有登录 就直接return true
        if ((!subject.isAuthenticated() && !subject.isRemembered())) {
            return Boolean.TRUE;
        }
        Session session = subject.getSession();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        String username = sysUser.getUsername();
        Serializable sessionId = session.getId();
        String onlineStatus = session.getAttribute(SysConfig.ONLINE_STATUS).toString();
        // 同步控制 双端队列depe
        Deque<Serializable> deque = cache.get(username);
        if (null == deque) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) &&
                (SysConfig.OnlineStatus.online.name().equals(onlineStatus) || SysConfig.OnlineStatus.hidden.name().equals(onlineStatus))) {
            deque.push(sessionId);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if (kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute(SysConfig.ONLINE_STATUS, SysConfig.OnlineStatus.kickout.name());
                }
            } catch (Exception e) {//ignore exception
            }
        }
        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (onlineStatus.equals(SysConfig.OnlineStatus.kickout.name())) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) { //ignore
            }
            saveRequest(servletRequest);
            WebUtils.issueRedirect(servletRequest, servletResponse, kickoutUrl);
            return false;
        }
        return true;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCache(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }

}
