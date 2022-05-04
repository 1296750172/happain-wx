package cn.happain.wx.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author happain
 * @since 2022-04-24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WxPerformer  {

    private String performerJpName;

    private String performerZhName;

    private String performerDate;

    private Integer performerAge;

    private String performerXingzuo;

    private String performerXuexing;

    private String performerSanweiT;

    private String performerSanweiB;

    private String performerSanweiW;

    private String performerSanweiH;

    private String performerZhaobei;

    private String performerCity;

    private String performerDesc;

    private Integer performerNum;

    private String performerImgServerUrl;

    @Override
    public String toString() {
        StringBuffer info = new StringBuffer();
        info.append("罩杯："+performerZhaobei+"\n");
        info.append("三围(B W H)："+performerSanweiB+" "+performerSanweiW+" "+performerSanweiH+"\n");
        info.append("身高："+performerSanweiT+"\n");
        info.append("中文名："+performerJpName+"\n");
        info.append("日文名："+performerZhName+"\n");
        info.append("年龄："+performerAge+"\n");
        info.append("出生日期："+performerDate+"\n");
        info.append("星座："+performerXingzuo+"\n");
        info.append("血型："+performerXuexing+"\n");
        info.append("城市："+performerCity+"\n");
        info.append("兴趣爱好："+performerDesc+"\n");
        info.append("作品数量："+performerNum+"\n");
        return info.toString();
    }
}
