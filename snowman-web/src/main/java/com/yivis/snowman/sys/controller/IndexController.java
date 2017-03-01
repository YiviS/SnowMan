package com.yivis.snowman.sys.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by XuGuang on 2017/2/14.
 * 系统登陆控制
 */
@Controller
@RequestMapping("/")
@Scope(value="prototype")
public class IndexController {

    /**
     * 登陆管理
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
        String message = (String) request.getAttribute("message");
        model.addAttribute("message", message);
        return "login";
    }

    @RequestMapping("/index")
    public String test1(Model model) {
        model.addAttribute("username", "super");
        return "index";
    }

    @RequestMapping("/")
    public String test3(Model model) {
        model.addAttribute("username", "super");
        return "index";
    }

}
