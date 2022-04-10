package cn.happain.wx.mapper;

import cn.happain.wx.pojo.vo.WxContent;
import cn.happain.wx.pojo.vo.WxUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface WxContentMapper extends BaseMapper<WxContent> {

}
