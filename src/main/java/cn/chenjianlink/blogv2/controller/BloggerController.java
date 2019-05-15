package cn.chenjianlink.blogv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 查看Master信息Controller
 *
 * @author chenjian
 */
@Controller
public class BloggerController {

    /**
     * "关于Master"页面展示
     *
     * @param model 页面视图模型
     * @return 页面
     */
    @GetMapping("/blogger/aboutMe")
    public String aboutMe(Model model) {
        ControllerMethod.showMainTemp(model);
        model.addAttribute("mainPage", "foreground/blogger/info.html");
        model.addAttribute("pageTitle", "关于Master-局外人之秘境");
        return "mainTemp";
    }

}
