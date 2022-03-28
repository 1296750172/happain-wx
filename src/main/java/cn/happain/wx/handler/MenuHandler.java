package cn.happain.wx.handler;

import cn.happain.wx.builder.TextBuilder;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.bo.WxUserCheck;
import cn.happain.wx.pojo.vo.WxUser;
import cn.happain.wx.server.EventService;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler implements WxMpMessageHandler {


    @Autowired
    private EventService eventService;
    @Autowired
    private UserMapper userMapper;
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        String msg = String.format("type:%s, event:%s, key:%s",
            wxMessage.getMsgType(), wxMessage.getEvent(),
            wxMessage.getEventKey());
        /*appid 哪个公众号*/
        String appId = weixinService.getWxMpConfigStorage().getAppId();
        System.out.println(appId);
        /*openId*/

        /*处理验证码事件*/
        if (EventType.CLICK.equals(wxMessage.getEvent()) && wxMessage.getEventKey().equals("LOGIN")) {



            WxUserCheck wxUserCheck = new WxUserCheck();
            /*设置openId*/
            wxUserCheck.setOpenId(wxMessage.getOpenId());
            /*设置用户名*/
            WxUser user = userMapper.selectOne(new QueryWrapper<WxUser>().eq("open_id", wxMessage.getOpenId()));
            wxUserCheck.setNickname(user.getNickname());
            /*设置验证码*/
            wxUserCheck.setCode("123456");

            JSONObject jsonObject = JSONUtil.parseObj(wxUserCheck);
            Message message = eventService.checkCode(appId,wxMessage.getOpenId(), jsonObject.toStringPretty());
            if(message.isOk()) {
                return new TextBuilder().build("内部错误，请重新点击",wxMessage,weixinService);
            }else {
                return new TextBuilder().build("内部错误，请重新点击",wxMessage,weixinService);
            }

        }


        return WxMpXmlOutMessage.TEXT().content(msg)
            .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
            .build();
    }

}
