package com.yivis.snowman.core.filter;

import com.yivis.snowman.core.utils.base.LoggerUtils;
import com.yivis.snowman.core.utils.base.Servlets;
import com.yivis.snowman.sys.entity.SysUserOnlineBo;
import net.sf.json.JSONObject;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
        HttpServletRequest httpRequest = ((HttpServletRequest) servletRequest);
        String url = httpRequest.getRequestURI();
        Subject subject = getSubject(servletRequest, servletResponse);
        //如果是相关目录 or 如果没有登录 就直接return true
        if (url.startsWith("/pc/") || (!subject.isAuthenticated() && !subject.isRemembered())) {
            return Boolean.TRUE;
        }
        Session session = subject.getSession();
        SysUserOnlineBo sysUserOnlineBo = (SysUserOnlineBo) subject.getPrincipal();
        String username = sysUserOnlineBo.getUsername();
        Serializable sessionId = session.getId();
        /**
         * 判断是否已经踢出
         * 1.如果是Ajax 访问，那么给予json返回值提示。
         * 2.如果是普通请求，直接跳转到登录页
         */
        Boolean marker = (Boolean) session.getAttribute(KICKOUT_STATUS);
        if (null != marker && marker) {
            Map<String, String> resultMap = new HashMap<String, String>();
            //判断是不是Ajax请求
            if (Servlets.isAjaxRequest(httpRequest)) {
                LoggerUtils.debug(getClass(), "当前用户已经在其他地方登录，并且是Ajax请求！");
                resultMap.put("user_status", "300");
                resultMap.put("message", "您已经在其他地方登录，请重新登录！");
                out(servletResponse, resultMap);
            }
            return Boolean.FALSE;
        }

        // 同步控制 双端队列depe
        Deque<Serializable> deque = cache.get(username);
        if (deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute(KICKOUT_STATUS) == null) {
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
                    kickoutSession.setAttribute(KICKOUT_STATUS, true);
                }
            } catch (Exception e) {//ignore exception
            }
        }

        return Boolean.TRUE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //先退出
        Subject subject = getSubject(servletRequest, servletResponse);
        subject.logout();
        WebUtils.getSavedRequest(servletRequest);
        //再重定向
        WebUtils.issueRedirect(servletRequest, servletResponse, kickoutUrl);
        return false;
    }

    /**
     * out输出
     */
    private void out(ServletResponse hresponse, Map<String, String> resultMap)
            throws IOException {
        try {
            hresponse.setCharacterEncoding("UTF-8");
            PrintWriter out = hresponse.getWriter();
            out.println(JSONObject.fromObject(resultMap).toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            LoggerUtils.error(getClass(), "KickoutSessionFilter.class 输出JSON异常，可以忽略。");
        }
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
