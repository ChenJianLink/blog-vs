package cn.chenjianlink.blogv2.controller;

import cn.chenjianlink.blogv2.utils.PageResult;
import cn.chenjianlink.blogv2.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 日志首页展示controller
 *
 * @author chenjian
 */
@Controller
public class IdexController {

    @Resource
    private BlogService blogService;
    @Resource
    private ControllerMethod controllerMethod;

    /**
     * 首页展示
     *
     * @param model          页面视图模型
     * @param typeId         日志类别id
     * @param releaseDateStr 日志发布日期区间
     * @param page           要请求页面页码
     * @param request        请求
     * @return 首页页面
     */
    @GetMapping("/homepage")
    public String index(Model model, @RequestParam(value = "typeId", required = false) Integer typeId, @RequestParam(value = "releaseDateStr", required = false) String releaseDateStr, @RequestParam(value = "page", required = false) Integer page, HttpServletRequest request) {
        //判断page是否为空,为空则设置为1
        if (page == null || page <= 0) {
            page = 1;
        }
        //将传入的日期以及日志类别封装到map中
        Map<String, Object> blogMap = new HashMap<>(3);
        blogMap.put("typeId", typeId);
        blogMap.put("releaseDateStr", releaseDateStr);
        //查询
        PageResult pageResult = blogService.findBlogList(page, blogMap);
        //获取请求的url，将URL截取并封装到结果中
        String url = controllerMethod.getUrl(request);
        pageResult.setUrl(url);
        controllerMethod.showMainTemp(model);
        model.addAttribute("blogList", pageResult.getPageList());
        model.addAttribute("page", pageResult);
        model.addAttribute("pageTitle", "局外人之秘境");
        model.addAttribute("mainPage", "foreground/blog/list.html");
        return "mainTemp";
    }

}