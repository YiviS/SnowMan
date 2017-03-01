package com.yivis.snowman.core.utils.base;

import com.yivis.snowman.sys.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * Created by XuGuang on 2017/2/21.
 * 用户工具类
 */
@Component
public class UserUtils {

    /**
     * 获取当前登录者对象
     */
    public static SysUser getSysUser() {
        try {
            Subject subject = SecurityUtils.getSubject();
            SysUser sysUser = (SysUser) subject.getPrincipal();
            if (sysUser != null) {
                return sysUser;
            }
        } catch (UnavailableSecurityManagerException e) {

        } catch (InvalidSessionException e) {

        }
        return null;
    }

}
