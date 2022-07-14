package com.itxiong.facepay.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itxiong.facepay.domain.Course;
import com.itxiong.facepay.domain.Order;

import java.util.List;

public interface OrderDao extends BaseMapper<Order> {
    //根据用户id查询课程列表
    List<Course> findCoursesByUid(String uid);
    //根据课程id查询订单
    Order findOrderByCid(String cid,String uid);
}
