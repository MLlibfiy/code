package com.shujia.controller;


import com.shujia.bean.Student;
import com.shujia.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;


    /**
     * 根据id查询学生基本信息
     *
     */
    @RequestMapping("/studentinfo")
    public Student findStudentById(String id){
        return studentService.findStudentById(id);
    }
}
