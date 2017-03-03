package com.yivis.snowman.sys.controller;

import com.yivis.snowman.core.shiro.token.TokenManager;
import com.yivis.snowman.core.utils.geetestCaptcha.GeetestBo;
import com.yivis.snowman.sys.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by XuGuang on 2017/2/14.
 * 系统登陆控制
 */
@Controller
@RequestMapping("/")
@Scope(value = "prototype")
public class IndexController {

    @Autowired
    private TokenManager tokenManager;

    private Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

    /**
     * 登陆管理
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/index")
    public String indexq() {
        return "index";
    }

    @RequestMapping(value = "submitLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitLogin(SysUser sysUser, GeetestBo geetestBo, Boolean rememberMe, HttpServletRequest request, HttpServletResponse response) {

        AuthenticationToken token = tokenManager.createToken(sysUser, geetestBo, rememberMe, request, response);
        Map<String, String> map = tokenManager.login(token);
        String url = "";
        if (map.get("status") == "200") {
            resultMap.put("status", 200);
            resultMap.put("message", "登录成功");
            url = request.getContextPath() + "/index";
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", map.get("message"));
            url = request.getContextPath() + "/login";
        }
        //跳转地址
        resultMap.put("back_url", url);
        return resultMap;
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

}
