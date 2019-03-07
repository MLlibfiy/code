package com.shujia.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PageController {

    @GetMapping("/index")
    public String index(){
        return "home"; //当浏览器输入/index时，会返回/templates/home.html页面
    }

    /**
     * 获取登录页面的控制器
     *
     * @return
     */
    @GetMapping("/loginview")
    public String login(){
        return "login"; //当浏览器输入/login，会返回/templates/login.html页面
    }


    @GetMapping("/registerview")
    public String register(){
        return "register"; //当浏览器输入/login，会返回/templates/login.html页面
    }

    @GetMapping("/modifypasswordview")
    public String modifypassword(){
        return "modifypassword"; //当浏览器输入/login，会返回/templates/login.html页面
    }

}
