package com.ty.wetalk.model;

import lombok.Data;

//跟随消息内容一起携带的json数据对应的对象
@Data
public class JsonDataWithMessageContent {
    String receiverAccount;
    String msgType;
}
