package cn.happain.wx.controller;

import cn.happain.wx.config.WxMpProperties;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
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
        if(index.equals("0")) {
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
            /*关于我们*/
            WxMenuButton about = new WxMenuButton();
            about.setName("关于我们");
            about.setKey("ABOUT");
            about.setType(MenuButtonType.CLICK);


            menu.getButtons().add(login);
            menu.getButtons().add(main);
            menu.getButtons().add(about);


            /*毕设业务*/
            WxMenuButton web = new WxMenuButton();
            web.setType(MenuButtonType.VIEW);
            web.setName("毕设服务");
            web.setKey("BISHE");
            web.setUrl("https://www.happain.cn");


            /*作者信息*/
            WxMenuButton user = new WxMenuButton();
            user.setType(MenuButtonType.CLICK);
            user.setName("联系作者");
            user.setKey("USER");
            main.getSubButtons().add(web);
            main.getSubButtons().add(user);
            this.wxService.switchover(appid);
            return this.wxService.getMenuService().menuCreate(menu);
        }
        if(index.equals("1")) {
            System.out.println("111111111");
            /*获取appid*/
            String appid = wxMpProperties.getConfigs().get(Integer.parseInt(index)).getAppId();
            /*创建菜单*/
            WxMenu menu = new WxMenu();
            /*网站*/
            WxMenuButton web = new WxMenuButton();
            web.setType(MenuButtonType.VIEW);
            web.setName("福利网站");
            web.setKey("FULIWEB");
            web.setUrl("https://www.happain.cn");
            /*登陆业务*/
            WxMenuButton work = new WxMenuButton();
            work.setName("功能介绍");
            work.setKey("WORK");
            work.setType(MenuButtonType.CLICK);
            /*关于我们*/

            menu.getButtons().add(web);
            menu.getButtons().add(work);
            this.wxService.switchover(appid);
            return this.wxService.getMenuService().menuCreate(menu);
        }
        return "error";
    }

    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013&token=&lang=zh_CN
     * 如果要创建个性化菜单，请设置matchrule属性
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @return 如果是个性化菜单，则返回menuid，否则返回null
     */
    @PostMapping("/createByJson")
    public String menuCreate(@PathVariable String appid, @RequestBody String json) throws WxErrorException {
        return this.wxService.switchoverTo(appid).getMenuService().menuCreate(json);
    }

    /**
     * <pre>
     * 自定义菜单删除接口
     * 详情请见: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141015&token=&lang=zh_CN
     * </pre>
     */
    @GetMapping("/delete")
    public void menuDelete(@PathVariable String appid) throws WxErrorException {
        this.wxService.switchoverTo(appid).getMenuService().menuDelete();
    }

    /**
     * <pre>
     * 删除个性化菜单接口
     * 详情请见: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @param menuId 个性化菜单的menuid
     */
    @GetMapping("/delete/{menuId}")
    public void menuDelete(@PathVariable String appid, @PathVariable String menuId) throws WxErrorException {
        this.wxService.switchoverTo(appid).getMenuService().menuDelete(menuId);
    }

    /**
     * <pre>
     * 自定义菜单查询接口
     * 详情请见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141014&token=&lang=zh_CN
     * </pre>
     */
    @GetMapping("/get")
    public WxMpMenu menuGet(@PathVariable String appid) throws WxErrorException {
        return this.wxService.switchoverTo(appid).getMenuService().menuGet();
    }

    /**
     * <pre>
     * 测试个性化菜单匹配结果
     * 详情请见: http://mp.weixin.qq.com/wiki/0/c48ccd12b69ae023159b4bfaa7c39c20.html
     * </pre>
     *
     * @param userid 可以是粉丝的OpenID，也可以是粉丝的微信号。
     */
    @GetMapping("/menuTryMatch/{userid}")
    public WxMenu menuTryMatch(@PathVariable String appid, @PathVariable String userid) throws WxErrorException {
        return this.wxService.switchoverTo(appid).getMenuService().menuTryMatch(userid);
    }

    /**
     * <pre>
     * 获取自定义菜单配置接口
     * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
     * 请注意：
     * 1、第三方平台开发者可以通过本接口，在旗下公众号将业务授权给你后，立即通过本接口检测公众号的自定义菜单配置，并通过接口再次给公众号设置好自动回复规则，以提升公众号运营者的业务体验。
     * 2、本接口与自定义菜单查询接口的不同之处在于，本接口无论公众号的接口是如何设置的，都能查询到接口，而自定义菜单查询接口则仅能查询到使用API设置的菜单配置。
     * 3、认证/未认证的服务号/订阅号，以及接口测试号，均拥有该接口权限。
     * 4、从第三方平台的公众号登录授权机制上来说，该接口从属于消息与菜单权限集。
     * 5、本接口中返回的图片/语音/视频为临时素材（临时素材每次获取都不同，3天内有效，通过素材管理-获取临时素材接口来获取这些素材），本接口返回的图文消息为永久素材素材（通过素材管理-获取永久素材接口来获取这些素材）。
     *  接口调用请求说明:
     * http请求方式: GET（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN
     * </pre>
     */
    @GetMapping("/getSelfMenuInfo")
    public WxMpGetSelfMenuInfoResult getSelfMenuInfo(@PathVariable String appid) throws WxErrorException {
        return this.wxService.switchoverTo(appid).getMenuService().getSelfMenuInfo();
    }
}
