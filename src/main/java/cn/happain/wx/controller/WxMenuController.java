package cn.happain.wx.controller;

import cn.happain.wx.config.WxMpProperties;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/menu")
public class WxMenuController {
    @Autowired
    private WxMpService wxService;
    @Autowired
    private WxMpProperties wxMpProperties;

    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013&token=&lang=zh_CN
     * 如果要创建个性化菜单，请设置matchrule属性
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @return 如果是个性化菜单，则返回menuid，否则返回null
     */
    @PostMapping("/create/{index}")
    public String menuCreate(@PathVariable String index, @RequestBody WxMenu menu) throws WxErrorException {
        return this.wxService.switchoverTo(index).getMenuService().menuCreate(menu);
    }

    @GetMapping("/create/{index}")
    public String menuCreateSample(@PathVariable String index) throws WxErrorException, MalformedURLException {
        /*获取appid*/
        String appid = wxMpProperties.getConfigs().get(Integer.parseInt(index)).getAppId();
        /*创建菜单*/
        WxMenu menu = new WxMenu();
        /*用户按钮  给微信二维码*/
        WxMenuButton main = new WxMenuButton();
        main.setName("咨询合作");




        /*登陆业务*/
        WxMenuButton login = new WxMenuButton();
        login.setName("验证码");
        login.setKey("LOGIN");
        login.setType(MenuButtonType.CLICK);
        /*测试*/
        WxMenuButton test = new WxMenuButton();
        test.setName("测试");
        test.setKey("TEST");
        test.setType(MenuButtonType.VIEW);
        test.setUrl("https://www.happain.cn");


        menu.getButtons().add(login);
        menu.getButtons().add(main);
        menu.getButtons().add(test);


        /*作者主站*/
        WxMenuButton web = new WxMenuButton();
        web.setType(MenuButtonType.VIEW);
        web.setName("我的主站");
        web.setKey("WEB");
        web.setUrl("https://www.happain.cn");


        /*作者信息*/
        WxMenuButton user = new WxMenuButton();
        user.setType(MenuButtonType.CLICK);
        user.setName("联系作者");
        user.setKey("USER");


        main.getSubButtons().add(web);
        main.getSubButtons().add(user);




        return this.wxService.getMenuService().menuCreate(menu);
    }

}
