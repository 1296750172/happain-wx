package cn.happain.wx.handler;

import cn.happain.wx.builder.ImageBuilder;
import cn.happain.wx.builder.ImageTextBuild;
import cn.happain.wx.builder.MsgCustomBuilder;
import cn.happain.wx.builder.TextBuilder;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.mapper.WxContentMapper;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.vo.WxContent;
import cn.happain.wx.pojo.vo.WxUser;
import cn.happain.wx.server.EventService;
import cn.happain.wx.server.WechatService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.soap.SOAPBinding;
import java.util.Map;


/*关注事件*/
@Component
@Slf4j
public class FocusHandler implements WxMpMessageHandler {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EventService eventService;
    @Autowired
    private WxContentMapper wxContentMapper;



    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());
        String appId = weixinService.getWxMpConfigStorage().getAppId();

        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                Message subscribe = eventService.subscribe(userWxInfo,appId);
                log.info(subscribe.toString());
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                log.info("该公众号没有获取用户信息权限！");
            }
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }
        /*处理关注后发送信息*/
        try {
            MsgCustomBuilder.textBuild(wxContentMapper.selectOne(new QueryWrapper<WxContent>().eq("type", "msb_focu")).getContent(),wxMessage,weixinService);
            return new ImageBuilder().build("uv2rQc-r4ru6XEYv837Astwxpva3cgLkKOvoxzgMyVPRdeqpMulbVZFU1HdX-iRu",wxMessage,weixinService);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage) throws Exception {

        //TODO
        return null;
    }

}
