package cn.happain.wx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeChatException  extends RuntimeException{

    /*状态码*/
    private String code;

    /*错误消息*/
    private String message;
}
