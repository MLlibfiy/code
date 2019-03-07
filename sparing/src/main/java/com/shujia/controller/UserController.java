package com.shujia.controller;

import com.shujia.bean.Message;
import com.shujia.bean.User;
import com.shujia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * 童虎登录注册控制器
 */

/**
 * 注解
 * 告诉spring 这个类是搞什么的
 * spring 启动之后会解析这个类。通过反射获取这个类的属性和方法
 */
@RestController
public class UserController {


    //多态，父类引用指向子类对象

    /**
     * 依赖注入，
     * spring 回去自己的上下文中找一个和这个属性类型匹配的对象注入到这个属性中
     */
    @Autowired
    private UserService userService;

    /**
     * http://localhost:8080/login?username=张三&password=123
     * <p>
     * 参数根据变量名匹配
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
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

    @RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
    public Message modifyPassword(String username, String onepassword, String twopassword, String threepassword) {
        User user = new User(username, onepassword);
        return userService.modiftPassword(user, twopassword, threepassword);
    }


}
