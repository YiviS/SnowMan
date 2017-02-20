package com.yivis.snowman.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by XuGuang on 2017/2/14.
 */
@Controller
@RequestMapping("/")
public class TestController {

    @RequestMapping("/login")
    public String test() {
        return "login";
    }

    @RequestMapping("/index")
    public String test1() {
        return "index";
    }
    @RequestMapping("/")
    public String test3(){
        return "index";
    }

}
