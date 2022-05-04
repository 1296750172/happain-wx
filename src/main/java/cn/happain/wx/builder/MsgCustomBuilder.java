package cn.happain.wx.builder;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;

/*主动发消息的构造模板*/
public class MsgCustomBuilder {
    public static String textJosn = "{\n" +
            "    \"touser\":\"{}\",\n" +
            "    \"msgtype\":\"text\",\n" +
            "    \"text\":\n" +
            "    {\n" +
            "         \"content\":\"{}\"\n" +
            "    }\n" +
            "}\n";

    public static String imageJson = "{\n" +
            "    \"touser\":\"{}\",\n" +
            "    \"msgtype\":\"image\",\n" +
            "    \"image\":\n" +
            "    {\n" +
            "      \"media_id\":\"{}\"\n" +
            "    }\n" +
            "}";
    /*文本构造*/
    public static String textBuild(String content, WxMpXmlMessage wxMessage,WxMpService services) throws WxErrorException {
        String accessToken = services.getAccessToken();
        String format = StrUtil.format(textJosn, wxMessage.getFromUser(), content);
        String result = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, format);
        return result;
    }

    /*图片构造*/
    public static String imageBuild(String imageid, WxMpXmlMessage wxMessage,WxMpService services) throws WxErrorException {
        String accessToken = services.getAccessToken();
        String format = StrUtil.format(imageJson, wxMessage.getFromUser(), imageid);
        String result = HttpUtil.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + accessToken, format);

        return result;
    }

}
