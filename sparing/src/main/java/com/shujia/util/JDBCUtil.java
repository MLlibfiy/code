package com.shujia.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * jdbc连接锅具
 */
public class JDBCUtil {


    private static String USERNAME;
    private static String PASSWORD;
    private static String URL;
    private static String DRIVER;

    /**
     *
     * 类加载的时候执行
     */
    static {
        DRIVER = "com.mysql.jdbc.Driver";
        //类加载，不需要每次都加载
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        URL = "jdbc:mysql://localhost:3306/student";
        USERNAME = "root";
        PASSWORD = "123456";


    }


    public static Connection getConnection() {

        Connection connection = null;
        try {
            //2、建立连接
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;

    }

}
