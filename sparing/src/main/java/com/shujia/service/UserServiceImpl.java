package com.shujia.service;

import com.shujia.bean.Message;
import com.shujia.bean.User;
import com.shujia.dao.UserDaoImpl;
import com.shujia.util.Md5Util;

public class UserServiceImpl {
    //多次使用，定义为成员变量
    private UserDaoImpl userDao = new UserDaoImpl();

    /**
     * 注册
     */
    public Message register(User user, String newPassword) {
        //1、判断两次密码是否一致
        if (!newPassword.equals(user.getPassword())) {
            return new Message(0, "两次密码不一致");
        }

        //2、判断用户名是否已存在

        User newUser = userDao.findByUserName(user.getUsernmae());
        //如果newUser 不等于null，说明用户名已存在
        if (newUser != null) {
            return new Message(0, "用户名已存在");
        }

        //3、注册

        //对密码加密
        user.setPassword(Md5Util.md5(user.getPassword()));

        int insert = userDao.insert(user);

        if (insert == -1) {
            return new Message(0, "服务器异常");
        }

        return new Message(0, "注册成功");
    }
}
