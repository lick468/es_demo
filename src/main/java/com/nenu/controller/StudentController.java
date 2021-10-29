package com.nenu.controller;

import com.nenu.domain.Student;
import com.nenu.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author ：lichuankang
 * @date ：2021/2/26 17:29
 * @description ：
 */
@Controller
public class StudentController {
    @Autowired
    private StudentServiceImpl studentService;

    @ResponseBody
    @RequestMapping(value = "/addS")
    public String addStudent(Integer id) {
        Student student = new Student();
        student.setName("测试" + id);
        student.setDesc("这是测试" + id + "的个人简介，这里是描述的文字");
        student.setId(id);
        studentService.addStudent(student, String.valueOf(student.getId()));
        return "success";
    }


    @ResponseBody
    @RequestMapping(value = "/getS")
    public Map<String, Object> getStudent(String id) {

        return studentService.getStudent(id);
    }


    @ResponseBody
    @RequestMapping(value = "/searchS")
    public List<Student> searchStudent(String desc) {

        return studentService.searchStudent("desc", desc);
    }


}
