package cn.happain.wx;

import cn.happain.wx.server.WechatService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WxApplicationTests {

    @Autowired
    public WechatService wechatService;
    @Test
    void contextLoads() throws WxErrorException {
        wechatService.test();
    }



}
