package com.itxiong.facepay.service.impl;

import com.itxiong.facepay.dao.OrderDao;
import com.itxiong.facepay.domain.Order;
import com.itxiong.facepay.domain.ResultInfo;
import com.itxiong.facepay.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Override
    public Order findOrderByCid(String cid,String uid) {
        return orderDao.findOrderByCid(cid,uid);
    }
    @Override
    public void updateOrderStatus(Map<String, String> param) {
        String oid = param.get("out_trade_no");
        Order order = orderDao.selectById(oid);

        order.setState("1");//改为支付

        orderDao.updateById(order);//更新
    }

    @Override
    public ResultInfo findOrderStatus(String oid) {
        Order order = orderDao.selectById(oid);
        if (order !=null && order.getState().equals("1")) {//已支付
            return new ResultInfo(true);
        } else {
            return new ResultInfo(false);//未支付
        }
    }

    @Override
    public void save(Order order) {
        orderDao.insert(order);
    }


}
