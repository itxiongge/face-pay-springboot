package com.itxiong.facepay.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于访问face++人脸系统的模板类
 */
@Component
@Getter
@Setter
@ConfigurationProperties("face.config")
public class FaceTemplate {

    @Autowired
    private RestTemplate restTemplate;

    private String apiKey;
    private String apiSecret;
    private String outerId;



    /**
     * 人脸检测
     * @param base64ImgData:检测的人脸图片数据
     * @return
     */
    public DetectResponseEntity detect(String base64ImgData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        //MultiData消息体构建器
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        //普通参数
        multipartBodyBuilder.part("api_key", apiKey);
        multipartBodyBuilder.part("api_secret", apiSecret);
        multipartBodyBuilder.part("return_attributes", "gender,age");
        //文件参数
        multipartBodyBuilder.part("image_base64", base64ImgData, MediaType.IMAGE_PNG);
        //构建Multi消息体
        MultiValueMap<String, HttpEntity<?>> mutipartBody = multipartBodyBuilder.build();

        ResponseEntity<DetectResponseEntity> responseEntity = restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/detect", mutipartBody, DetectResponseEntity.class);

        return responseEntity.getBody();
    }
    //人脸检测结果封装类
    @Data
    public static class DetectResponseEntity {
        private String request_id;
        private Integer face_num;
        private List<FaceEntity> faces;
        //人脸结果封装类
        @Data
        public static class FaceEntity {
            private String face_token;
            private Map<String,HashMap<String,String>> attributes;
        }
    }
    /**
     * 人脸库-人脸搜索
     * @return
     */
    public SearchFaceResult faceSearch(String faceToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        //请求参数
        multipartBodyBuilder.part("api_key", apiKey);
        multipartBodyBuilder.part("api_secret", apiSecret);
        multipartBodyBuilder.part("face_token", faceToken);
        multipartBodyBuilder.part("outer_id", outerId);

        //构建消息体
        MultiValueMap<String, HttpEntity<?>> mutipartBody = multipartBodyBuilder.build();

        try{
            ResponseEntity<FaceSearchResponseEntity> responseEntity = restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/search", mutipartBody, FaceSearchResponseEntity.class);
            FaceSearchResponseEntity faceSearchResponseEntity = responseEntity.getBody();
            //搜索失败
            List<SearchFaceResult> results = faceSearchResponseEntity.getResults();
            if (results == null) {
                return new SearchFaceResult();
            }
            //置信度大于1e-5【十万分之一】
            if (results.get(0).confidence < faceSearchResponseEntity.getThresholds().e5) {
                return new SearchFaceResult();
            }
            //搜索成功
            return faceSearchResponseEntity.getResults().get(0);
        } catch (Exception e){
            e.printStackTrace();//打印异常
            return new SearchFaceResult();
        }


    }
    //人脸搜索
    @Data
    public static class FaceSearchResponseEntity {
        private String request_id;
        private Integer face_num;
        private List<SearchFaceResult> results;
        private ConfidenceThresholds thresholds; //置信度阈值
    }
    @Data
    public static class SearchFaceResult {
        private String face_token;
        private String user_id;
        private Double confidence;//置信度
    }

    /**
     * 人脸库-添加人脸
     * @param faceTokens 多个face_token用逗号分隔
     */
    public void addFaceToFaceSet(String faceTokens) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", apiKey);
        map.add("api_secret", apiSecret);
        map.add("outer_id", outerId);
        map.add("face_tokens", faceTokens);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/faceset/addface", request, String.class);

    }
    /**
     * 人脸库-自定义人脸信息【人脸与用户id绑定】
     */
    public BindingResponseEntity bindingUserIdWithFaceToken(String faceToken, String uid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", apiKey);
        map.add("api_secret", apiSecret);
        map.add("face_token", faceToken);
        map.add("user_id", uid);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<BindingResponseEntity> bindingResponseEntity = restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/face/setuserid", request, BindingResponseEntity.class);
        return bindingResponseEntity.getBody();

    }
    //自定义人脸信息返回结果封装类
    @Data
    public static class BindingResponseEntity {
        private String request_id;
        private String face_token;
        private String user_id;
        private String error_message;
    }

    /**
     * 移除人脸
     * @param faceToken
     */
    public void removeFace(String faceToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", apiKey);
        map.add("api_secret", apiSecret);
        map.add("outer_id", outerId);
        map.add("face_tokens", faceToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface", request, String.class);

    }
    /**
     * 人脸相似度对比
     * @param faceToken1
     * @param faceToken2
     * @return
     */
    public boolean compareFace(String faceToken1, String faceToken2) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        //普通域
        multipartBodyBuilder.part("api_key", apiKey);
        multipartBodyBuilder.part("api_secret", apiSecret);
        multipartBodyBuilder.part("face_token1", faceToken1);
        multipartBodyBuilder.part("face_token2", faceToken2);

        //build完整的消息体
        MultiValueMap<String, HttpEntity<?>> mutipartBody = multipartBodyBuilder.build();

        ResponseEntity<CompareResponseEntity> responseEntity = restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/compare", mutipartBody, CompareResponseEntity.class);

        System.out.println(responseEntity);
        CompareResponseEntity e = responseEntity.getBody();
        if (e.getConfidence() >= e.getThresholds().e5) {
            return true;
        } else {
            return false;
        }
    }

    //人脸比对返回结果封装类
    @Data
    public static class CompareResponseEntity {
        private Double confidence; //置信度
        private ConfidenceThresholds thresholds; //置信度阈值
    }

    /**
     * 置信度阈值封装类
     * 1e-3：误识率为千分之一的置信度阈值；
     * 1e-4：误识率为万分之一的置信度阈值；
     * 1e-5：误识率为十万分之一的置信度阈值；
     */
    @Data
    public static class ConfidenceThresholds {
        @JsonProperty("1e-5")
        private Double e5;
        @JsonProperty("1e-4")
        private Double e4;
        @JsonProperty("1e-3")
        private Double e3;
    }
    /**
     * 创建人脸库【不用】
     * outer_id=face_pay
     */
    public void facesetCreate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", apiKey);
        map.add("api_secret", apiSecret);
        map.add("outer_id", outerId);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/faceset/create", request, String.class);
    }


    /**
     * 获取人脸库【不用】
     */
    public FaceSetResponseEntity getFaceSetDetail() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", apiKey);
        map.add("api_secret", apiSecret);
        map.add("outer_id", outerId);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<FaceSetResponseEntity> responseEntity = restTemplate.postForEntity("https://api-cn.faceplusplus.com/facepp/v3/faceset/getdetail", request, FaceSetResponseEntity.class);
        return responseEntity.getBody();
    }
    //人脸库返回结果封装类
    @Data
    public static class FaceSetResponseEntity {
        private String outer_id;
        private Integer face_count;
        private List<String> face_tokens;
    }
}
