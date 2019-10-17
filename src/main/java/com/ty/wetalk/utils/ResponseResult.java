package com.ty.wetalk.utils;

import lombok.Data;

@Data
public class ResponseResult {
    public String status = "1";//返回1则表示请求成功，0则表示请求成功，但是不是正常数据
    public Object data;
    public String tip;

    public ResponseResult() {
    }

    public ResponseResult( String status, Object data, String tip) {
        this.status = status;
        this.data = data;
        this.tip = tip;
    }

}
