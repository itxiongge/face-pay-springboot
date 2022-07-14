package com.itxiong.facepay.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultInfo {

    private Boolean success;
    private String message;
    private Object data;

    // 一个参数构造
    public ResultInfo(Boolean success) {
        this.success = success;
    }

    // 二个参数构造
    public ResultInfo(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // 三个参数构造
    public ResultInfo(Boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
