package cn.happain.wx.pojo;


import lombok.Data;

@Data

public class Check {

    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;

}
