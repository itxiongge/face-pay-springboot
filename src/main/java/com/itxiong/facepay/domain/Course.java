package com.itxiong.facepay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tab_course")
public class Course implements Serializable {

    @TableId(type = IdType.INPUT)
    private String cid;
    private String courseName;
    private Double price;//售价
    private String listPrice;//划线价
    private Date startTime;
    private String img;

}
