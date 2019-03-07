package com.shujia.dao;

import com.shujia.bean.User;

public interface UserDao {

    User findByUserName(String username);

    int insert(User user);
}
