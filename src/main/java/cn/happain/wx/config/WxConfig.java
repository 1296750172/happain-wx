package cn.happain.wx.config;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class WxConfig {

    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId("wxfa69f82b8aca1cfd");
        config.setSecret("711d36ded78bb130ebcea0669aad304a");
        config.setToken("xiaozeguai");
        config.setAesKey("qallswMD8eGbn7YfzXxiYZz6BKIDnMH1dpMQNgftJeF");
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);
        return wxService;
    }




}
