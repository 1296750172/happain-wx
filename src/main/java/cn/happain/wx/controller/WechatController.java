package cn.happain.wx.controller;

import cn.happain.wx.config.WxConfig;
import cn.happain.wx.config.WxMpProperties;
import cn.happain.wx.pojo.Check;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.server.WechatService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.j2objc.annotations.AutoreleasePool;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.PriorityQueue;

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

    /*获取用户消息*/
    @PostMapping("/{index}")
    public String getmessage(@PathVariable String index,HttpServletRequest httpServletRequest, HttpServerResponse httpServerResponse) throws IOException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        WxMpXmlMessage message= WxMpXmlMessage.fromXml(httpServletRequest.getInputStream());
        String xml = wechatService.dealMessage(Integer.parseInt(index),message);
        return xml;
    }

    /*获取accessToken*/
    @GetMapping("/getAccessToken/{index}")
    public Message getAccessToken(@PathVariable String index) {

        return wechatService.getToken(Integer.parseInt(index));
    }




}
