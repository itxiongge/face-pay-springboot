package com.itxiong.facepay.service;

import com.itxiong.facepay.domain.User;

public interface FaceService {

    /**
     * 人脸登录
     * @param base64ImgData
     * @return
     */
    User loginByFace(String base64ImgData,String filePath);
}
