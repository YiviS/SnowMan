package com.yivis.snowman.sys.controller;

import com.yivis.snowman.core.aop.log.UserLog;
import com.yivis.snowman.core.utils.poi.ExcelRead;
import com.yivis.snowman.sys.entity.SysUser;
import com.yivis.snowman.sys.entity.SysUserLog;
import com.yivis.snowman.sys.service.SysUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    @UserLog(code = "getUserLogList", name = "查询日志 ", remarkClass = SysUserLog.class)
    public List<SysUserLog> getUserLogList(HttpServletRequest request) {
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

    @RequestMapping("/toReadExcel")
    public String indexq() {
        return "readExcel";
    }

    /**
     * readExcel
     */
    @RequestMapping(value = "/readExcel")
    @ResponseBody
    public List readExcel(@RequestParam(value = "excelFile") MultipartFile file, HttpServletRequest reques) {
        Excel excel = new Excel();
        List list = excel.test111(file);
        return list;
    }

    public class Excel extends ExcelRead<SysUser> {
        public List test111(MultipartFile file) {
            String[] rule = {"id","username","password"};
            SysUser sysUser = new SysUser();
            return this.parserExcel(sysUser,rule,file);
        }
    }
}
