package cn.happain.wx.server;

import cn.happain.wx.consts.WxConst;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.bo.WxUserCheck;
import cn.happain.wx.pojo.vo.WxUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisPool jedisPool;

    /*关注*/
    public Message subscribe(WxMpUser wxUser,String appid) {
        Message message1 = new Message();
        Date date = new Date();
        /*如果不为空*/
        WxUser user = userMapper.selectOne(new QueryWrapper<WxUser>().eq("open_id", wxUser.getOpenId()));
        if(user!=null) {
            user.setSubscribe(true);
            user.setSubscribeTime(date);
            user.setCreateTime(date);
            user.setUpdateTime(date);
            int open_id = userMapper.update(user, new UpdateWrapper<WxUser>().eq("open_id", user.getOpenId()));
            if(open_id==-1) {
                Message message = new Message();
                message.fail("修改失败");
                return message;
            }
        }
        /*添加用户*/
        else {
            WxUser wxUser1 = new WxUser();
            wxUser1.setOpenId(wxUser.getOpenId());
            wxUser1.setAppId(appid);
            wxUser1.setNickname(wxUser.getOpenId());
            wxUser1.setSubscribe(true);
            wxUser1.setSubscribeTime(date);
            wxUser1.setCreateTime(date);
            wxUser1.setUpdateTime(date);
            int insert = userMapper.insert(wxUser1);
            if (insert==-1) {
                Message message = new Message();
                message.fail("修改失败");
                return message;
            }

        }
        message1.success("ok");
        return message1;

    }

    /*取消关注*/
    public Message unsubscribe(String openId) {
        Message message = new Message();
        WxUser user = new WxUser();
        user.setSubscribe(false);

        int open_id = userMapper.update(user, new UpdateWrapper<WxUser>().eq("open_id", openId));
        if(open_id!=-1){
            message.success("ok");
        }else {
            message.fail("更新错误");
        }
        return message;
    }

    /*验证码事件*/
    public Message checkCode(String appid, String openid, String data) {
        Message message = new Message();
        Jedis jedis = jedisPool.getResource();
        String key = appid + "@" + openid;
        jedis.set(key,data);
        jedis.expire(key, WxConst.SECOND);
        message.success("ok");
        return message;
    }
}
