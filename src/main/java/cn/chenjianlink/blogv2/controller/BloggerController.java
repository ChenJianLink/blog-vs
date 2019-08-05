package cn.chenjianlink.blogv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * 查看Master信息Controller
 *
 * @author chenjian
 */
@Controller
public class BloggerController {

    @Resource
    private ControllerMethod controllerMethod;

    /**
     * "关于Master"页面展示
     *
     * @param model 页面视图模型
     * @return 页面
     */
    @GetMapping("/blogger/aboutMe")
    public String aboutMe(Model model) {
        controllerMethod.showMainTemp(model);
        model.addAttribute("mainPage", "foreground/blogger/info.html");
        model.addAttribute("pageTitle", "关于Master-局外人之秘境");
        return "mainTemp";
    }

}
