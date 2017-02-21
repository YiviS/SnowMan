package com.yivis.snowman.core.shiro;

import com.yivis.snowman.sys.entity.SysUser;


/**
 * Created by XuGuang on 2017/2/21.
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class Principal extends SysUser {

    private static final long serialVersionUID = 1L;

    private Integer id; // 编号
    private String username; // 登录名
    private String name; // 姓名

    public Principal(Integer id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
}
