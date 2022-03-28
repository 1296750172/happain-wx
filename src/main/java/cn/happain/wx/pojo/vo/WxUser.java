package cn.happain.wx.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxUser {

    @TableId
    private Long id;
    private String openId;
    private Boolean subscribe;
    private String nickname;
    private Date subscribeTime;
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;
    @TableField(fill= FieldFill.UPDATE)
    private Date updateTime;
}
