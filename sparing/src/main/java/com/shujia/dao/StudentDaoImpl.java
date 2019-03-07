package com.shujia.dao;

import com.shujia.bean.Student;
import com.shujia.util.JDBCUtil;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class StudentDaoImpl implements StudentDao{
    @Override
    public Student findStudentById(String id) {

        Student student = new Student();

        Connection connection = JDBCUtil.getConnection();
        String sql = "select * from student where id=?";
        try {
            PreparedStatement stat = connection.prepareStatement(sql);
            stat.setString(1,id);
            ResultSet resultSet = stat.executeQuery();

            if (resultSet.next()){
                student.setId(id);
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                student.setGender(resultSet.getString("gender"));
                student.setClazz(resultSet.getString("clazz"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return student;
    }
}
