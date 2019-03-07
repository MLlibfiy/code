package com.shujia.service;

import com.shujia.bean.Message;
import com.shujia.bean.User;
import com.shujia.dao.UserDao;
import com.shujia.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 告诉spring这个类是业务层的一个类，之后就不需要我们自己再new 这个类的对象了
 */

@Service
public class UserServiceImpl implements UserService {
    //多次使用，定义为成员变量

    @Autowired
    private UserDao userDao;

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

    //修改密码
    @Override
    public Message modiftPassword(User user, String twoPassword, String threePassword) {

        if (!twoPassword.equals(threePassword)) {
            return new Message(0, "两次输入密码不一致");
        }

        //1、判断用户名密码是否正确
        User newUser = userDao.findByUserName(user.getUsernmae());
        if (newUser == null) {
            return new Message(0, "用户名不存在");
        }

        if (!newUser.getPassword().equals(Md5Util.md5(user.getPassword()))) {
            return new Message(0, "原密码输入错误");
        }

        //修改密码
        int i = userDao.updatePasswordByName(user.getUsernmae(), Md5Util.md5(twoPassword));

        if (i == -1) {
            return new Message(0, "密码修改失败");
        }

        return new Message(1, "密码修改成功");
    }
}
