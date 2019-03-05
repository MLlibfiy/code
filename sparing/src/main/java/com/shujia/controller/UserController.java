package com.shujia.controller;

import com.shujia.bean.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 童虎登录注册控制器
 */
@RestController
public class UserController {

    /**
     * http://localhost:8080/login?username=张三&password=123
     * <p>
     * 参数根据变量名匹配
     */
    @RequestMapping("/login")
    public Message login(String username, String password) {

        /**
         * 访问数据库查询用户名和密码是否正确
         */

        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2、建立连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "123456");

            //3、获取执行器
            Statement stat = connection.createStatement();

            String sql = "select * from user where username='" + username + "' and password='" + password + "'";

            //执行查询
            ResultSet resultSet = stat.executeQuery(sql);

            //有结果返回登录成功

            /**
             * 一个正常的接口返回需要的东西
             * 1、状态码  必须有
             * 2、数据内容   可选
             *
             */
            if (resultSet.next()) {
                Message msg = new Message(1, "登录成功");
                //spring 会自动将对象转成json字符串
                return msg;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Message msg = new Message(0, "用户名密码错误");
        //spring 会自动将对象转成json字符串
        return msg;
    }

    @RequestMapping("/register")
    public Message register(String username, String password, String newpassword) {
        //1、判断吗是否一致
        if (!password.equals(newpassword)) {
            return new Message(0, "两次输入的密码不一致");
        }

        //2、查询用户是否已存在
        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2、建立连接
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student", "root", "123456");

            //3、获取执行器
            Statement stat = connection.createStatement();

            String sql = "select * from user where username='" + username + "'";

            //执行查询
            ResultSet resultSet = stat.executeQuery(sql);
            //有结果返回登录成功
            /**
             * 一个正常的接口返回需要的东西
             * 1、状态码  必须有
             * 2、数据内容   可选
             */
            if (resultSet.next()) {
                //spring 会自动将对象转成json字符串
                return new Message(0, "用户已存在");
            }

            //3、注册

            String registerSql = "insert into user(username,password) values('" + username + "','" + password + "')";
            //影响的行数
            int i = stat.executeUpdate(registerSql);

            if (i==1){
                return new Message(1, "注册成功");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Message(0, "注册失败");
    }
}
