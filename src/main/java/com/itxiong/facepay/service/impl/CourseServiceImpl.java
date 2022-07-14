package com.itxiong.facepay.service.impl;

import com.itxiong.facepay.dao.CourseDao;
import com.itxiong.facepay.dao.OrderDao;
import com.itxiong.facepay.domain.Course;
import com.itxiong.facepay.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Course findCourseCid(String cid) {
        Course course = courseDao.selectById(cid);
        return course;
    }

    @Override
    public List<Course> findCoursesByUid(String uid) {
        return orderDao.findCoursesByUid(uid);
    }
}
