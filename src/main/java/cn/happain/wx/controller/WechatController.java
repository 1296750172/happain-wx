package cn.happain.wx.controller;

import cn.happain.wx.config.WxMpProperties;
import cn.happain.wx.server.WechatService;
import cn.hutool.http.server.HttpServerResponse;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/wx/home")
public class WechatController {

    @Autowired
    private WxMpService wxService;
    @Autowired
    private WxMpProperties wxMpProperties;
    @Autowired
    private WechatService wechatService;
    /*验证消息有效性*/
    @GetMapping("/{index}")
    public String wxcheck(@PathVariable String index,HttpServletRequest httpServletRequest) {

        String echostr = httpServletRequest.getAttribute("echostr").toString();
        return echostr;
    }

    /*获取用户发送的消息*/
    @PostMapping("/{index}")
    public String getmessage(@PathVariable String index,HttpServletRequest httpServletRequest, HttpServerResponse httpServerResponse) throws IOException {
        System.out.println("11111111111");
        httpServletRequest.setCharacterEncoding("UTF-8");
        WxMpXmlMessage message= WxMpXmlMessage.fromXml(httpServletRequest.getInputStream());
        String xml = wechatService.dealMessage(Integer.parseInt(index),message);
        return xml;
    }

    /*获取accessToken*//*
    @GetMapping("/getAccessToken/{index}")
    public Message getAccessToken(@PathVariable String index) {

        return wechatService.getToken(Integer.parseInt(index));
    }*/




}
