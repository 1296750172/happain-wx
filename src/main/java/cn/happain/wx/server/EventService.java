package cn.happain.wx.server;

import cn.happain.wx.consts.WxConst;
import cn.happain.wx.mapper.UserMapper;
import cn.happain.wx.pojo.Message;
import cn.happain.wx.pojo.bo.WxUserCheck;
import cn.happain.wx.pojo.vo.WxUser;
import cn.happain.wx.utils.CodeUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;

@Service
public class EventService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisPool jedisPool;

    /*优优资料卡关注*/
    public Message youyou_subscribe(String openid,String appid) {
        Message message1 = new Message();
        Date date = new Date();
        /*如果不为空*/
        WxUser user = userMapper.selectOne(new QueryWrapper<WxUser>().eq("open_id", openid).eq("app_id",appid));
        if(user!=null) {
            user.setSubscribe(true);
            user.setSubscribeTime(date);
            user.setCreateTime(date);
            user.setUpdateTime(date);
            int open_id = userMapper.update(user, new UpdateWrapper<WxUser>().eq("open_id", user.getOpenId()).eq("app_id",appid));
            if(open_id==-1) {
                Message message = new Message();
                message.fail("修改失败");
                return message;
            }
        }
        /*添加用户*/
        else {
            WxUser wxUser1 = new WxUser();
            wxUser1.setOpenId(openid);
            wxUser1.setAppId(appid);
            wxUser1.setNickname(openid);
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

    /*关注*/
    public Message subscribe(WxMpUser wxUser,String appid) {
        Message message1 = new Message();
        Date date = new Date();
        /*如果不为空*/
        WxUser user = userMapper.selectOne(new QueryWrapper<WxUser>().eq("open_id", wxUser.getOpenId()).eq("app_id",appid));
        if(user!=null) {
            user.setSubscribe(true);
            user.setSubscribeTime(date);
            user.setCreateTime(date);
            user.setUpdateTime(date);
            int open_id = userMapper.update(user, new UpdateWrapper<WxUser>().eq("open_id", user.getOpenId()).eq("app_id",appid));
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
    public Message unsubscribe(String openId,String appid) {
        Message message = new Message();
        WxUser user = new WxUser();
        user.setSubscribe(false);
        int open_id = userMapper.update(user, new UpdateWrapper<WxUser>().eq("open_id", openId).eq("app_id",appid));
        if(open_id!=-1){
            message.success("ok");
        }else {
            message.fail("更新错误");
        }
        return message;
    }

    /*验证码事件*/
    public Message checkCode(String appid, String openid, WxUserCheck wxUserCheck) {
        Message message = new Message();
        /*验证码*/
        String code = CodeUtil.getCode();
        wxUserCheck.setCode(code);
        Jedis jedis = jedisPool.getResource();
        String key = appid + "@" + code;
        jedis.set(key, JSONUtil.parseObj(wxUserCheck).toStringPretty());
        jedis.expire(key, WxConst.SECOND);
        message.success("ok");
        message.setMessage(code);
        return message;
    }


    /*验证码事件*/
    public Message youyou_checkCode(String appid, String openid, WxUserCheck wxUserCheck) {
        Message message = new Message();
        /*验证码*/
        String code = CodeUtil.getCode();
        wxUserCheck.setCode(code);
        Jedis jedis = jedisPool.getResource();
        String key = appid + "@" + code;
        jedis.set(key, JSONUtil.parseObj(wxUserCheck).toStringPretty());
        jedis.expire(key, WxConst.SECOND);
        message.success("ok");
        message.setMessage(code);
        return message;
    }
}
