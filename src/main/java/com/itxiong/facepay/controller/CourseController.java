package com.itxiong.facepay.controller;

import com.itxiong.facepay.domain.Course;
import com.itxiong.facepay.domain.User;
import com.itxiong.facepay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/course/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    //跳转到商品页面
    @RequestMapping("/toSku")
    public String find(Model model) {
        return "sku";
    }
    //跳转到我的课程
    @RequestMapping("/toMyCourse")
    public String toAdd(HttpServletRequest request, Model model) {
        //判断是否登陆
        User user = (User) request.getSession().getAttribute("currentUser");
        //如果登陆，则进入到我的课程
        if (user != null) {
            List<Course> courses = courseService.findCoursesByUid(user.getUid());
            user.setCourseList(courses);
            model.addAttribute("user", user);
        } else {
            return "forward:/face/toFace?from=myCourse";//请求转发
        }
        //如果没有登陆，则直接扫脸登陆
        return "mycourse";
    }


}
