package com.shujia.controller;

import com.shujia.bean.Message;
import com.shujia.bean.User;
import com.shujia.service.UserService;
import com.shujia.service.UserServiceImpl;
import com.shujia.util.Md5Util;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * 童虎登录注册控制器
 */
@RestController
public class UserController {


    //多态，父类引用指向子类对象
    private UserService userService = new UserServiceImpl();

    /**
     * http://localhost:8080/login?username=张三&password=123
     * <p>
     * 参数根据变量名匹配
     */
    @RequestMapping("/login")
    public Message login(String username, String password) {
        User user = new User();
        user.setUsernmae(username);
        user.setPassword(password);
        //调用业务层登录方法
        return userService.login(user);
    }

    @RequestMapping("/register")
    public Message register(String username, String password, String newpassword) {

        User user = new User(username, password);
        //调用业务层的注册方法
        Message msg = userService.register(user, newpassword);
        return msg;
    }
}
