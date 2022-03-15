package cn.happain.wx.server;

import cn.happain.wx.pojo.Message;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WechatService {
    @Autowired
    public WxMpService wxMpService;


    public Message test() throws WxErrorException {
        String[] callbackIP = wxMpService.getCallbackIP();
        return null;
    }
}
