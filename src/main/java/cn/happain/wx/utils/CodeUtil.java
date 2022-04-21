package cn.happain.wx.utils;

import cn.hutool.core.util.RandomUtil;


/*生成验证码*/
public class CodeUtil {


    /*验证码类*/
    public static String getCode(){
        return RandomUtil.randomString("0123456789",6);
    }

    public static void main(String[] args) {
        String s = RandomUtil.randomString("0123456789abcdefg",6);
        System.out.println(s);
    }
}
