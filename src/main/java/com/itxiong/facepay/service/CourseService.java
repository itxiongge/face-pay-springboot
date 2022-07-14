package com.itxiong.facepay.service;

import com.itxiong.facepay.domain.Course;

import java.util.List;

public interface CourseService {

    /**
     * 根据cid查询课程详情
     * @param cid
     * @return
     */
    Course findCourseCid(String cid);
    //根据用户id查询课程订单信息
    List<Course> findCoursesByUid(String uid);
}
