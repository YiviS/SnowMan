package com.yivis.snowman.sys.controller;

import com.yivis.snowman.sys.entity.SysUserLog;
import com.yivis.snowman.sys.service.SysUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by XuGuang on 2017/3/7.
 */
@Controller
@RequestMapping("user")
public class SysUserController {
    @Autowired
    private SysUserLogService sysUserLogService;

    /**
     * 查询用户日志
     */
    @RequestMapping("getUserLogList")
    @ResponseBody
    public List<SysUserLog> getUserLogList() {
        return sysUserLogService.getList(new SysUserLog());
    }

    /**
     * 查询用户日志
     */
    @RequestMapping("getUserLogMapList")
    @ResponseBody
    public List<Map> getUserLogMapList() {
        return sysUserLogService.getMapList();
    }
}
