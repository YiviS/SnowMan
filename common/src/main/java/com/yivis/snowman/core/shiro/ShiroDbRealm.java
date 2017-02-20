package com.yivis.snowman.core.shiro;

import com.yivis.snowman.sys.entity.SysUser;
import com.yivis.snowman.sys.service.SysService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


/**
 * Created by XuGuang on 2017/2/16.
 */
@Service
public class ShiroDbRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(getClass());
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    @Autowired
    private SysService sysService;

    /**
     * 认证回调函数, 登录时调用.获取身份验证信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        ExtendUsernamePasswordToken token = (ExtendUsernamePasswordToken) authenticationToken;

        String username = token.getUsername();
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }
        // 校验用户名密码
        SysUser user = sysService.getUserByLoginName(username);
        if (null == user) {
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }
        byte[] salt = new String(user.getUsername()).getBytes();

        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(salt), getName());
    }

    /**
     * 授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        SysUser shiroUser = (SysUser) principalCollection.getPrimaryPrincipal();
        SysUser sysUser = sysService.getUserByLoginName(shiroUser.getUsername());
        if (sysUser != null) {
            return info;
        } else {
            return null;
        }
    }

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(HASH_ALGORITHM);
        matcher.setHashIterations(HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

}