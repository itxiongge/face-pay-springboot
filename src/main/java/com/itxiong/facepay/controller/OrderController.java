package com.itxiong.facepay.controller;

import com.itxiong.facepay.domain.Course;
import com.itxiong.facepay.domain.Order;
import com.itxiong.facepay.domain.ResultInfo;
import com.itxiong.facepay.domain.User;
import com.itxiong.facepay.service.CourseService;
import com.itxiong.facepay.service.OrderService;
import com.itxiong.facepay.utils.PayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private OrderService orderService;

    //跳转到刷脸界面
    @RequestMapping("/createOrder")
    public String createOrder(HttpServletRequest request, Model model) {
        //获取Session会话中用户对象
        User user = (User) request.getSession().getAttribute("currentUser");
        String cid = request.getParameter("cid");
        //查询课程信息
        Course course = courseService.findCourseCid(cid);

        //防止重复购买，跳转到sku页面
        Order orderDo = orderService.findOrderByCid(cid, user.getUid());
        if (orderDo != null) {
            model.addAttribute("msg", "请勿重复购买课程！");
            return "sku";
        }

        //创建订单
        Order order = new Order(course.getPrice(), user, course.getCid());
        orderService.save(order);
        //调用PayUtils获取支付链接地址
        //跳转支付界面，展示订单信息，支付二维码
        //注意:金额需要进行换算，要元单位换算为分 1元 == 100分
        String pay_url = PayUtils.createOrder(order.getOid(), 1);

        model.addAttribute("pay_url", pay_url);
        model.addAttribute("course", course);
        model.addAttribute("order", order);

        return "pay";
    }
    //人脸登陆
    @RequestMapping("/findPayStatus")
    @ResponseBody
    public ResultInfo faceDetect(String oid) {
        //1.接收请求参数: oid
        //2.调用Service接口，返回订单的支付状态ResultInfo对象
        ResultInfo resultInfo = orderService.findOrderStatus(oid);
        return resultInfo;
    }
}
