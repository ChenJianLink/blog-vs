package cn.chenjianlink.blogv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 访问可视化Controller
 *
 * @author chenjian
 */
@Controller
public class VisiterShowController {

    @Resource
    private ControllerMethod controllerMethod;
    /**
     * 展示可视化页面
     *
     * @param model 页面模型
     * @return 主页面
     */
    @GetMapping("/show/visiterShow")
    public String visiterShow(Model model) {
        controllerMethod.showMainTemp(model);
        model.addAttribute("mainPage", "foreground/visitershow/show.html");
        model.addAttribute("pageTitle", "访客记录-局外人之秘境");
        return "mainTemp";
    }
}
