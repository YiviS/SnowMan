package com.yivis.snowman.sys.service;

import com.yivis.snowman.sys.entity.SysUser;
import org.springframework.stereotype.Service;

/**
 * Created by XuGuang on 2017/2/16.
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 */
@Service
public class SysService {

    /**
     *  根据登录名获取用户
     *
     * @param username 用户名
     * @Author XuGuang
     * @Date 2017/2/16 16:34
     */
    public SysUser getUserByLoginName(String username){
        //TODO 根据登录名获取用户
        SysUser sysUser = new SysUser();
        sysUser.setUsername("super");
        sysUser.setPassword("a72982d91c5b9cf054826734494b54024e59e851");
        return sysUser;
    }

}
