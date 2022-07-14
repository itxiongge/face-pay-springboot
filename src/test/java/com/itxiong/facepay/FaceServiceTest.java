package com.itxiong.facepay;

import com.itxiong.facepay.dao.UserDao;
import com.itxiong.facepay.domain.User;
import com.itxiong.facepay.service.FaceService;
import com.itxiong.facepay.utils.FaceTemplate;
import com.itxiong.facepay.utils.ImageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FaceServiceTest {

    @Autowired
    private FaceService faceService;
    @Autowired
    private FaceTemplate faceTemplate;
    @Autowired
    private UserDao userDao;

    @Test
    public void loginByFace() {
        String imageBase64 = ImageUtils.imageBase64("C:\\Users\\Administrator\\Pictures\\漂亮脸蛋\\5358fc2a8dfd4c079df884086f9833cc.png");
        FaceTemplate.DetectResponseEntity e = faceTemplate.detect(imageBase64);
        String filePath = "/upload/pic/5358fc2a8dfd4c079df884086f9833cc.png";
        User user = faceService.loginByFace(imageBase64, filePath);
        System.out.println("user = " + user);//
    }

    /**
     * 雄哥：dfece9533814f413155a92be0f5b6f0a
     * 关关：0e074f9dc3a2114c43c19ff3df777a8a
     *
     * 雄哥：
     *     2ec6dd6c83bef4ec3acbb7a6e1d962c1
     *     2fbf202550d131ae248db7db15b73f86
     *     200cd3d4b6ab499840a4ad2e32eca44d
     *     dfcb84038c4299e9378ee0edc66b4255
     *     51c03aaf096c9012dbfa6e62a9089f07
     */
    @Test
    public void removeUser(){
        List<String> uids = new ArrayList<>();
        uids.add("1001711422406656");
        for (String uid : uids) {
            User user = userDao.selectById(uid);
            String faceToken = user.getFaceToken();
            faceTemplate.removeFace(faceToken);
            userDao.deleteById(user.getUid());
        }
    }

}
