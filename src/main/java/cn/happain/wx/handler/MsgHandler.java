package cn.happain.wx.handler;

import cn.happain.wx.builder.ImageTextBuild;
import cn.happain.wx.builder.TextBuilder;
import cn.happain.wx.config.WxMpProperties;
import cn.happain.wx.consts.WxConst;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.bo.WxUserCheck;
import cn.happain.wx.pojo.vo.WxPerformer;
import cn.happain.wx.server.EventService;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class MsgHandler implements WxMpMessageHandler {

    @Autowired
    private WxMpProperties wxMpProperties;
    @Autowired
    private EventService eventService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String appId = weixinService.getWxMpConfigStorage().getAppId();
        if(appId.equals(wxMpProperties.getConfigs().get(0).getAppId())) {

        }
        if(appId.equals(wxMpProperties.getConfigs().get(1).getAppId())) {
            String content = wxMessage.getContent().trim();
            if("验证码".equals(content)) {
                WxUserCheck wxUserCheck = new WxUserCheck();
                /*设置openId*/
                wxUserCheck.setOpenId(wxMessage.getFromUser());
                wxUserCheck.setNickname(wxMessage.getFromUser());
                Message message = eventService.youyou_checkCode(appId, wxMessage.getFromUser(), wxUserCheck);
                if(message.isOk()) {
                    return new TextBuilder().build("验证码: "+message.getMessage(),wxMessage,weixinService);
                }else {
                    return new TextBuilder().build("内部错误，请重新点击",wxMessage,weixinService);
                }
            }
            /*查询演员资料*/
            if(content.startsWith("yy")) {
                String[] split = content.split(" ");
                if(split.length>1){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("pageNum",0);
                    map.put("pageSize",10);
                    map.put("performerName",split[1]);
                    String post = HttpUtil.post(WxConst.YOUYOU_URL + "youyou/performer/query", JSONUtil.parse(map).toStringPretty());
                    JSONObject jsonObject = JSONUtil.parseObj(post);
                    if("200".equals(jsonObject.get("code").toString())){
                        JSONArray data = JSONUtil.parseArray(JSONUtil.parseObj(jsonObject.get("data").toString()).get("list").toString());
                        /*如果查询到的话*/
                        if(data.size()>0){
                            WxPerformer wxPerformer = JSONUtil.toBean(JSONUtil.parseObj(data.get(0).toString()), WxPerformer.class);

                            /*发送图文消息*/
                            return new ImageTextBuild().build(wxPerformer.toString(),wxPerformer.getPerformerImgServerUrl(),wxPerformer.getPerformerZhName(),"https://happain.cn",wxMessage,weixinService);
                        }else {
                            return new TextBuilder().build("未搜索到优优信息",wxMessage,weixinService);
                        }
                    }else {
                        return new TextBuilder().build("未搜索到优优信息",wxMessage,weixinService);
                    }
                }else {
                    return new TextBuilder().build("命令格式错误,请输入yy 作为前缀",wxMessage,weixinService);
                }
            }
        }
        return new TextBuilder().build("你好", wxMessage, weixinService);

    }

}
