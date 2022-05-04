package cn.happain.wx.config;

import cn.happain.wx.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

@Configuration
public class WxConfig {

    @Autowired
    private WxMpProperties wxMpProperties;
    @Autowired
    private LogHandler logHandler;
    @Autowired
    private MsgHandler msgHandler;
    @Autowired
    private FocusCancelHandler focusCancelHandler;
    @Autowired
    private FocusHandler focusHandler;
    @Autowired
    private MenuHandler menuHandler;
    @Autowired
    private ViewHandler viewHandler;



    @Bean
    public WxMpService wxMpService() {
        /*获取我的微信公众号的配置*/
        List<WxMpProperties.MpConfig> configs = wxMpProperties.getConfigs();
        WxMpService wxService = new WxMpServiceImpl();
        /*遍历公众号配置的对象*/
        wxService.setMultiConfigStorages(configs.stream().map(a-> {
            WxMpDefaultConfigImpl configStorage;
            /*如果有使用redis*/
            if (wxMpProperties.isUseRedis()) {

                configStorage = new WxMpDefaultConfigImpl();

            } else {
                /*没有使用redis*/
                configStorage = new WxMpDefaultConfigImpl();
            }
            configStorage.setAppId(a.getAppId());
            configStorage.setSecret(a.getSecret());
            configStorage.setToken(a.getToken());
            configStorage.setAesKey(a.getAesKey());
            return configStorage;
        }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));;
        return wxService;
    }


    /*消息路由*/
    @Bean
    public WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
        WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(logHandler).next();
        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(focusHandler).end();
        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(focusCancelHandler).end();
        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.CLICK).handler(menuHandler).end();
        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.VIEW).handler(viewHandler).end();


       /* // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.CLICK).handler(this.menuHandler).end();
        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.VIEW).handler(this.nullHandler).end();
        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(this.subscribeHandler).end();
        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(this.unsubscribeHandler).end();
        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.LOCATION).handler(this.locationHandler).end();
        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XmlMsgType.LOCATION).handler(this.locationHandler).end();
        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SCAN).handler(this.scanHandler).end();*/
        // 默认
        newRouter.rule().async(false).handler(msgHandler).end();
        return newRouter;
    }




}
