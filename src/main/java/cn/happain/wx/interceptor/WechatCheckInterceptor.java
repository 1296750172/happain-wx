package cn.happain.wx.interceptor;


import cn.happain.wx.config.WxMpProperties;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.WeChatException;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/*
* 验证消息签名是否正确
* */
@Slf4j
public class WechatCheckInterceptor implements HandlerInterceptor {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMpProperties wxMpProperties;


    public WechatCheckInterceptor(WxMpService wxMpService, WxMpProperties wxMpProperties) {
        this.wxMpService = wxMpService;
        this.wxMpProperties = wxMpProperties;
    }

    /*请求前*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception,WeChatException {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        String[] split = request.getServletPath().split("/");
        String index = split[split.length-1];
        System.out.println(index);
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);
        log.info("接收到来自微信服务器的认证消息：[{},{}, {}, {}, {}]",
                index,
                signature,
                timestamp, nonce, echostr);
        /**/
        if (StringUtils.isAnyBlank(signature, timestamp, nonce)) {
            response.setStatus(500);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Message message = new Message();
            message.setCode("500");
            log.info("请求参数非法，请核实!");
            message.setMessage("请求参数非法，请核实!");
            PrintWriter out = response.getWriter();
            out.append(JSONUtil.parseObj(message).toJSONString(2));
            return false;

        }
       /* 验证appID是否有效*/
        if (!wxMpService.switchover(wxMpProperties.getConfigs().get(Integer.parseInt(index)).getAppId())) {
            response.setStatus(500);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Message message = new Message();
            message.setCode("500");
            message.setMessage("未找到对应appid配置，请核实！");
            log.info("未找到对应appid配置，请核实！");
            PrintWriter out = response.getWriter();
            out.append(JSONUtil.parseObj(message).toJSONString(2));
            return false;
        }
        /*验证消息是否合法*/
        if(!wxMpService.checkSignature(request.getParameter("timestamp"), request.getParameter("nonce"), request.getParameter("signature"))){
            response.setStatus(500);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            Message message = new Message();
            message.setCode("500");
            log.info("消息不合法");
            message.setMessage("消息不合法");
            PrintWriter out = response.getWriter();
            out.append(JSONUtil.parseObj(message).toJSONString(2));
            return false;
        }
        request.setAttribute("echostr",echostr);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
