package cn.happain.wx.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisConfig {

    @Autowired
    private WxMpProperties wxMpProperties;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        WxMpProperties.RedisConfig redisConfig = wxMpProperties.getRedisConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout(), redisConfig.getPassword());
        return jedisPool;
    }

}
