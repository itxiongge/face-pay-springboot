package com.itxiong.facepay.service.impl;

import com.itxiong.facepay.dao.UserDao;
import com.itxiong.facepay.domain.User;
import com.itxiong.facepay.utils.FaceTemplate;
import com.itxiong.facepay.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FaceServiceImpl implements FaceService {

    @Autowired
    private FaceTemplate faceTemplate;
    @Autowired
    private UserDao userDao;


    @Override
    public User loginByFace(String base64ImgData,String filePath) {
        //1. 人脸检测获取face_token
        FaceTemplate.DetectResponseEntity faceDetectResp = faceTemplate.detect(base64ImgData);
        FaceTemplate.DetectResponseEntity.FaceEntity faceDetectEntity = faceDetectResp.getFaces().get(0);
        if (faceDetectEntity == null) {
            return null;
        }
        String face_token = faceDetectEntity.getFace_token();
        //2. 人脸搜索，找到找的人脸
        FaceTemplate.SearchFaceResult searchFaceResult = faceTemplate.faceSearch(face_token);
        //获取人脸信息
        String user_id = searchFaceResult.getUser_id();
        //3. 没有找到【置信度阈值低于1e-5】
        if (user_id == null) {
            //   1. 初始化用户信息，获取用户uuid
            String gender = faceDetectEntity.getAttributes().get("gender").get("value");
            User user = new User(gender, filePath, face_token);
            userDao.insert(user);
            //   2. 添加人脸到人脸库
            faceTemplate.addFaceToFaceSet(face_token);
            // 接口调用间歇时间不能太短，会触发限流
            try {
                Thread.sleep(1100);//休眠1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //   3. 自定义人脸信息
            faceTemplate.bindingUserIdWithFaceToken(face_token, user.getUid());
            return user;

        }
        //4. 找到【置信度阈值高于1e-5】，查询用户信息，执行确认授权
        User user = userDao.selectById(user_id);
        return user;
    }

}
