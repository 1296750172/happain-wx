package cn.happain.wx.server;

import cn.happain.wx.config.WxMpProperties;
import cn.happain.wx.handler.WxDefaultHandle;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.vo.WxUser;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WechatService {
    @Autowired
    public WxMpService wxMpService;
    @Autowired
    private WxMpProperties wxMpProperties;
    @Autowired
    private WxMpMessageRouter wxMpMessageRouter;

    /*获取access token*/
    public Message getToken(int index) {
        /*获取对应的公众号id*/
        WxMpProperties.MpConfig mpConfig = wxMpProperties.getConfigs().get(index);
        /**/
        Message message = new Message();
        HttpResponse response = HttpRequest.get(StrUtil.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}", mpConfig.getAppId(), mpConfig.getSecret())).execute();
        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        message.success(jsonObject.get("access_token").toString());
        return message;
    }

    /*处理消息*/
    public String dealMessage(int index,WxMpXmlMessage message) {
        WxMpProperties.MpConfig mpConfig = wxMpProperties.getConfigs().get(index);

        WxMpXmlOutMessage r = wxMpMessageRouter.route(message);
        return r.toXml();
    }


}
