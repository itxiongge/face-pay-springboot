package com.itxiong.facepay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itxiong.facepay.utils.IdWorker;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Data
@TableName("tab_user")
public class User implements Serializable {

    @TableId(type = IdType.INPUT)
    private String uid;
    private String username;
    private String password;
    private String telephone;
    private String nickname;
    private String sex;
    private Date birthday;
    private String email;
    private String pic;
    private String faceToken;

    @TableField(exist = false)
    private List<Course> courseList;//课程列表

    public User() {
    }

    public User(String sex, String pic, String faceToken) {
        IdWorker idWorker = new IdWorker(0, 0);
        this.uid = String.valueOf(idWorker.nextId());
        this.password = "e10adc3949ba59abbe56e057f20f883e";//123456
        this.nickname = randomName(sex);
        this.sex = sex;
        this.pic = pic;
        this.faceToken = faceToken;
    }
    public static String randomName(String sex){
        List<String> list = new ArrayList<>();
        if (sex.equals("Female")){
            list.add("西施");
            list.add("王昭君");
            list.add("貂蝉");
            list.add("杨玉环");
        } else {
            list.add("潘安");
            list.add("宋玉");
            list.add("卫玠");
            list.add("兰陵王高长恭");
        }
        //根据性别随机抽取一个
        return list.get(new Random().nextInt(8));
    }

}
