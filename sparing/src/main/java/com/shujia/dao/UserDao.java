package com.shujia.dao;

import com.shujia.bean.User;

public interface UserDao {

    User findByUserName(String username);

    int insert(User user);

    //根据用户名修改密码
    int updatePasswordByName(String username,String newPassword);
}
