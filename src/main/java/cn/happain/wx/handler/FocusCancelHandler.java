package cn.happain.wx.handler;

import cn.happain.wx.pojo.Message;
import cn.happain.wx.server.EventService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/*取消关注事件*/
@Component
@Slf4j
public class FocusCancelHandler implements WxMpMessageHandler {

    @Autowired
    private EventService eventService;
    /*更新数据库状态*/
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        String openId = wxMessage.getFromUser();
        String appId = weixinService.getWxMpConfigStorage().getAppId();
        System.out.println("取消关注"+openId);
        log.info("取消关注用户 OPENID: " + openId);
        Message unsubscribe = eventService.unsubscribe(openId,appId);
        log.info(unsubscribe.toString());
        // TODO 可以更新本地数据库为取消关注状态
        return null;
    }



}
