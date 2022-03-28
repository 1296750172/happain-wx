package cn.happain.wx.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

public class ImageTextBuild {
    public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage,
                                   WxMpService service) {


        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setDescription("description");
        item.setPicUrl("http://e0a7-183-253-229-47.ngrok.io/image/fire.png");
        item.setTitle("title");
        item.setUrl("https://www.happain.cn");

        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .addArticle(item)
                .build();

        return m;
    }
}
