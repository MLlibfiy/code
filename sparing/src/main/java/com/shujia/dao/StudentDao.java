package com.shujia.dao;

import com.shujia.bean.Student;

public interface StudentDao {

    //根据id查询学生
    Student findStudentById(String id);
}
