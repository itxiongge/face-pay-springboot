package com.itxiong.facepay.service;

import com.itxiong.facepay.domain.Order;
import com.itxiong.facepay.domain.ResultInfo;

import java.util.Map;

public interface OrderService {
    //修改订单支付状态
    void updateOrderStatus(Map<String, String> param);

    //查询订单状态
    ResultInfo findOrderStatus(String oid);

    //创建订单
    void save(Order order);

    //根据课程id查询订单
    Order findOrderByCid(String cid, String uid);
}
