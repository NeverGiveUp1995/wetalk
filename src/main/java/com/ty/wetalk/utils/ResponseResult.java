package com.ty.wetalk.utils;

import lombok.Data;

@Data
public class ResponseResult {
    /*
     * 消息类型：0：系统心跳消息，1：聊天消息。。。【待添加】
     * */
    public String msgType = "0";
    public String status = "1";//返回1则表示请求成功，0则表示请求成功，但是不是正常数据
    public Object data;
    public String tip;

    public ResponseResult() {
    }

    public ResponseResult(String msgType, String status, Object data, String tip) {
        this.msgType = msgType;
        this.status = status;
        this.data = data;
        this.tip = tip;
    }

}
