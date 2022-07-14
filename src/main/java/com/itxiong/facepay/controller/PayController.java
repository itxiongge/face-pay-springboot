package com.itxiong.facepay.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itxiong.facepay.domain.Course;
import com.itxiong.facepay.domain.Order;
import com.itxiong.facepay.domain.User;
import com.itxiong.facepay.service.CourseService;
import com.itxiong.facepay.service.OrderService;
import com.itxiong.facepay.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pay/")
public class PayController {

    @Autowired
    private OrderService orderService;

    //跳转支付成功
    @RequestMapping("/toSuccess")
    public String toSuccess() {
        return "success";
    }
    //跳转支付失败
    @RequestMapping("/toFail")
    public String toFail() {
        return "fail";
    }
    /**
     * 微信支付成功回调
     * @param request
     * @param response
     * @throws IOException
     */
    //跳转到刷脸界面
    @RequestMapping("/notify")
    public void createOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.接收微信传递来请求参数 : 请求体的xml中 输入流方式传递
        ServletInputStream in = request.getInputStream();
        //2.将请求体中的XML输入流转换为Map对象。Jackson，给我们提供的XmlMapper 类似于ObjectMapper
        XmlMapper xmlMapper = new XmlMapper();
        Map<String, String> param = xmlMapper.readValue(in, Map.class);
        //3.调用OrderService修改订单状态
        orderService.updateOrderStatus(param);
        //4.响应微信，已接收成功【向微信，输出一个XML的字符串】
        // <xml>
        //     <result_code>success</result_code>
        //     <result_msg>ok</result_msg>
        // </xml>
        Map<String, String> result = new HashMap<>();
        result.put("result_code", "success");
        result.put("result_msg", "ok");
        String xmlString = xmlMapper.writeValueAsString(result);
        //向微信输出结果
        //响应给微信的时候，最好告知当前的数据类型
        response.setContentType("application/xml;charset=utf-8");
        response.getWriter().write(xmlString);
    }
}
