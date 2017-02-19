package com.yivis.snowman.sys.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by XuGuang on 2017/2/14.
 */
@Controller
@RequestMapping("/")
public class TestController {

    @RequestMapping("/")
    @RequiresRoles("")
    public String test() {
        return "index";
    }
}
