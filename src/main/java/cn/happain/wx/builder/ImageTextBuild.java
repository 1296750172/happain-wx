package cn.happain.wx.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;

public class ImageTextBuild {
    public WxMpXmlOutMessage build(String desc,String picurl,String title,String url, WxMpXmlMessage wxMessage,
                                   WxMpService service) {


        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        item.setDescription(desc);
        item.setPicUrl(picurl);
        item.setTitle(title);
        item.setUrl(url);
        WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .addArticle(item)
                .build();

        return m;
    }
}
