package com.shujia.service;

import com.shujia.bean.Message;
import com.shujia.bean.User;

/**
 * 业务层接口
 */
public interface UserService {

    /**
     *一般由架构师定义号接口中的方法，由开发人员来做具体实现
     *
     *
     */
    Message register(User user, String newpasword);


    //登录
    Message login(User user);


}
