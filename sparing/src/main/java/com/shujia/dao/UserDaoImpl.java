package com.shujia.dao;

import com.shujia.bean.User;
import com.shujia.util.JDBCUtil;
import com.shujia.util.Md5Util;

import java.sql.*;

/**
 * 数据库访问层
 */
public class UserDaoImpl {

    /**
     * 根据用户名查询用户
     */
    public User findByUserName(String username) {

        User user = null;
        //获取连接
        Connection connection = JDBCUtil.getConnection();
        try {
            String sql = "select * from user where username=?";
            //获取预编译sql执行器
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setString(1, username);

            ResultSet resultSet = stat.executeQuery();
            //如果有查到数据，user 就不是null了
            if (resultSet.next()) {
                user = new User();
                //通过列名获取值
                user.setUsernmae(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * 插入一条数据
     */

    public int insert(User user) {
        Connection connection = JDBCUtil.getConnection();

        String sql = "insert into user(username,password) values(?,?)";

        try {
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setString(1, user.getUsernmae());
            stat.setString(2, user.getPassword());

            return stat.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
