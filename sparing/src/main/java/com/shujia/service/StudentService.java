package com.shujia.service;

import com.shujia.bean.Student;
import org.springframework.stereotype.Service;


public interface StudentService {

    //根据id查询学生
    Student findStudentById(String id);
}
