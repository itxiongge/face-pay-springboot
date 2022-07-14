package com.itxiong.facepay.controller;

import com.itxiong.facepay.domain.ResultInfo;
import com.itxiong.facepay.domain.User;
import com.itxiong.facepay.service.FaceService;
import com.itxiong.facepay.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@Controller
@RequestMapping("/face/")
public class FaceController {

    @Autowired
    private FaceService faceService;

    //跳转到刷脸界面
    @RequestMapping("/toFace")
    public String toFace(String cid,String from,Model model) {
        model.addAttribute("cid", cid);//课程cid
        model.addAttribute("from", from);//刷脸来源：1.刷脸支付、2.我的课程
        return "face";
    }

    @RequestMapping("/toApprove")
    public String toApprove(HttpServletRequest request,Model model) {
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        String cid = request.getParameter("cid");
        String from = request.getParameter("from");
        model.addAttribute("user", currentUser);
        model.addAttribute("cid", cid);
        model.addAttribute("from", from);
        return "approve";
    }

    //刷脸
    @RequestMapping("/faceDetect")
    @ResponseBody
    public ResultInfo faceDetect(@RequestParam("imgData") String imgData, HttpServletRequest request) {
        //获取文件存储路径
        String savePath = "src/main/resources/static/upload/pic/";
        File savePos = new File(savePath);
        if(!savePos.exists()){  // 不存在，则创建该文件夹
            savePos.mkdir();
        }
        //创建文件名称
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
        //base64编码图片数据生成图片
        String base64Img = imgData.substring(22);

        ImageUtils.generateImage(base64Img, savePos.getAbsolutePath()+File.separator, fileName);
        String filePath = "/upload/pic/" + fileName;
        System.out.println("savePath = " + savePath + fileName);
        System.out.println("filePath = " + filePath);
        //登陆
        User user = faceService.loginByFace(base64Img,filePath);
        //设置用户信息到session中
        request.getSession().setAttribute("currentUser", user);
        if (user != null) {
            return new ResultInfo(true,"识别成功");
        } else {
            return new ResultInfo(false,"识别人脸失败");
        }

    }
}
