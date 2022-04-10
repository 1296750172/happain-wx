package cn.happain.wx.handler;

import cn.happain.wx.builder.ImageBuilder;
import cn.happain.wx.builder.ImageTextBuild;
import cn.happain.wx.builder.MsgCustomBuilder;
import cn.happain.wx.builder.TextBuilder;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.mapper.WxContentMapper;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.bo.WxUserCheck;
import cn.happain.wx.pojo.vo.WxContent;
import cn.happain.wx.pojo.vo.WxUser;
import cn.happain.wx.server.EventService;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.http.HttpBase;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpMaterialServiceImpl;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNewsBatchGetResult;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private WxContentMapper wxContentMapper;
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        /*appid 哪个公众号*/
        String appId = weixinService.getWxMpConfigStorage().getAppId();
        /*处理验证码事件*/
        if (EventType.CLICK.equals(wxMessage.getEvent()) && wxMessage.getEventKey().equals("LOGIN")) {

            
            WxUserCheck wxUserCheck = new WxUserCheck();

            /*设置openId*/
            wxUserCheck.setOpenId(wxMessage.getFromUser());
            /*设置用户名*/
            WxUser user = userMapper.selectOne(new QueryWrapper<WxUser>().eq("open_id", wxMessage.getFromUser()));
            /*如果查不到用户  临时添加一个*/
            if(user==null) {
                user  = new WxUser();
                user.setNickname(wxMessage.getFromUser());
                user.setOpenId(wxMessage.getFromUser());
                user.setSubscribe(true);
                user.setSubscribeTime(new Date());
                user.setCreateTime(new Date());
                userMapper.insert(user);
            }

            wxUserCheck.setNickname(user.getNickname());
            /*设置验证码*/
            RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
            LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200,100);
            lineCaptcha.setGenerator(randomGenerator);
            lineCaptcha.createCode();
            String code = lineCaptcha.getCode();
            wxUserCheck.setCode(code);

            JSONObject jsonObject = JSONUtil.parseObj(wxUserCheck);
            Message message = eventService.checkCode(appId,wxMessage.getFromUser(), jsonObject.toStringPretty());
            if(message.isOk()) {
                return new TextBuilder().build("验证码: "+code,wxMessage,weixinService);
            }else {
                return new TextBuilder().build("内部错误，请重新点击",wxMessage,weixinService);
            }

        }
        /*测试按钮*/
        if(EventType.CLICK.equals(wxMessage.getEvent()) && wxMessage.getEventKey().equals("ABOUT")) {
           /* WxMpMaterialService materialService = weixinService.getMaterialService();
            try {
                WxMpMaterialFileBatchGetResult image = materialService.materialFileBatchGet("image", 0, 20);
                List<WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem> items = image.getItems();

            } catch (WxErrorException e) {
                e.printStackTrace();
            }*/
            //那天微信开发文档推送给关注者
            MsgCustomBuilder.textBuild( wxContentMapper.selectOne(new QueryWrapper<WxContent>().eq("type", "msb_about")).getContent(), wxMessage,weixinService);
            return new ImageBuilder().build("uv2rQc-r4ru6XEYv837Astwxpva3cgLkKOvoxzgMyVPRdeqpMulbVZFU1HdX-iRu",wxMessage,weixinService);

        }


        return WxMpXmlOutMessage.TEXT().content("未识别操作")
            .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
            .build();
    }

}
