package com.itxiong.facepay;

import com.itxiong.facepay.utils.FaceTemplate;
import com.itxiong.facepay.utils.ImageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FaceTemplateTest {

    @Autowired
    private FaceTemplate faceTemplate;

    @Test
    public void detect() {
        String imageBase64 = ImageUtils.imageBase64("C:\\Users\\Administrator\\Pictures\\漂亮脸蛋\\5358fc2a8dfd4c079df884086f9833cc.png");
        FaceTemplate.DetectResponseEntity e = faceTemplate.detect(imageBase64);
        System.out.println(e);
    }

    @Test
    public void getFaceSetDetail() {
        FaceTemplate.FaceSetResponseEntity e = faceTemplate.getFaceSetDetail();
        System.out.println(e);
    }

    @Test
    public void createFaceSet() {
        faceTemplate.facesetCreate();
    }

    @Test
    public void addFaceToFaceSet() {
        faceTemplate.addFaceToFaceSet("dfcb84038c4299e9378ee0edc66b4255");
    }

    @Test
    public void campareFace() {
        boolean b = faceTemplate.compareFace("dfcb84038c4299e9378ee0edc66b4255", "51c03aaf096c9012dbfa6e62a9089f07");
        System.out.println(b);
    }


}
