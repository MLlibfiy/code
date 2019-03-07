package com.shujia.service;

import com.shujia.bean.Message;
import com.shujia.bean.User;
import com.shujia.dao.UserDao;
import com.shujia.dao.UserDaoImpl;
import com.shujia.util.Md5Util;

public class UserServiceImpl implements UserService {
    //多次使用，定义为成员变量
    private UserDao userDao = new UserDaoImpl();

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


    //登录实现
    @Override
    public Message login(User user) {

        //1、查询账号是否存在
        User newUser = userDao.findByUserName(user.getUsernmae());
        if (newUser == null) {
            return new Message(0, "用户名输入错误");
        }

        //对密码加密
        String password = Md5Util.md5(user.getPassword());

        //判断密码是否正确
        if (password.equals(newUser.getPassword())) {
            return new Message(1, "登录成功");
        }

        return new Message(0, "密码错误");
    }
}
