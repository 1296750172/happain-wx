package cn.happain.wx.utils;



/*
*   微信消息加解密
* */
public class WxCrypt {


 /*   *//*解密*//*
    public static WxMpXmlMessage decode(WxMpXmlOutMessage message, WxMpService wxMpService) {
        String s = message.toEncryptedXml(wxMpService.getWxMpConfigStorage());

        return message;
    }

    *//*加密*//*
    public static WxMpXmlMessage encode(WxMpXmlMessage message,) {
        inMessage = WxMpXmlMessage.fromEncryptedXml();

        return message;
    }*/

}
