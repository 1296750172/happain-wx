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
public class WxContent {

    @TableId
    private Long id;

    private String content;
    private String type;

    @TableField(fill= FieldFill.INSERT)
    private Date createTime;
}
