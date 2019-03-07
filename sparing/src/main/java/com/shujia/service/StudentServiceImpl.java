package com.shujia.service;

import com.shujia.bean.Student;
import com.shujia.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentDao studentDao;

    @Override
    public Student findStudentById(String id) {
        return studentDao.findStudentById(id);
    }
}
