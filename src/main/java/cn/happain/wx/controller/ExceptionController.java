package cn.happain.wx.controller;

import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.WeChatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class ExceptionController {


    /*捕获自定义异常*/
    @ExceptionHandler(WeChatException.class)
    public Message weChatExceptionHandler(HttpServletRequest req, WeChatException e) {
        System.out.println(e.getMessage());
        log.error(e.getMessage());
        Message message = new Message(e.getCode(), e.getMessage());
        return message;
    }

    /*捕获全局异常*/
    @ExceptionHandler(Exception.class)
    public Message globalExceptionHandler(HttpServletRequest req, Exception e) {
        System.out.println(e.getMessage());
        log.error(e.getMessage());
        Message message = new Message("500",e.getMessage());
        return message;
    }
}
