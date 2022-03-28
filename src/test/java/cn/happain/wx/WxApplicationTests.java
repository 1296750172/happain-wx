package cn.happain.wx;

import cn.happain.wx.config.RedisConfig;
import cn.happain.wx.config.WxMpProperties;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.server.WechatService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;

@SpringBootTest
class WxApplicationTests {

    @Autowired
    private JedisPool jedisPool;
    @Test
    void contextLoads() throws WxErrorException {
        Jedis resource = jedisPool.getResource();
        HashMap<String, String> map = new HashMap<>();
        map.put("aaa","1");
        Long demo = resource.hset("demo", map);
        System.out.println(demo);
    }



}
