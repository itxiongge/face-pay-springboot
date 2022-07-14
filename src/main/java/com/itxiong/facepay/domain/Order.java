package com.itxiong.facepay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@TableName("tab_order")
public class Order implements Serializable {

    @TableId(type = IdType.INPUT)
    private String oid;
    private Date orderTime;//订单创建时间
    private Double total; //订单总金额
    private String state;//支付状态，1已支付，0未支付
    private String address;//收货地址
    private String contact;//联系方式
    private String telephone;//手机号
    private String uid;//用户id
    private String cid;//课程id

    public Order() {
    }

    public Order(Double total,  User user, String cid) {
        this.oid = UUID.randomUUID().toString().replaceAll("-","");
        this.orderTime = new Date();
        this.total = total;
        this.state = "0";//未支付
        this.contact = user.getNickname();
        this.telephone = user.getTelephone();
        this.uid = user.getUid();
        this.cid = cid;
    }
}
