package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.BlogType;
import cn.chenjianlink.blogv2.service.BlogService;
import cn.chenjianlink.blogv2.service.BlogTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台页面跳转显示Controller
 *
 * @author chenjian
 */
@Controller
@RequestMapping("/admin")
public class ForwordController {

    @Resource
    private BlogTypeService blogTypeService;
    @Resource
    private BlogService blogService;

    private static final String WRITEBLOG = "writeBlog";

    /**
     * 后台页面跳转
     *
     * @param page  要跳转的页面名称
     * @param model 页面视图模型
     * @return 页面
     */
    @GetMapping("/page/{page}")
    public String forwordPage(@PathVariable(value = "page") String page, Model model, @RequestParam(value = "isUeditor", required = false) Boolean isUeditor) {
        //判断是否为写日志的页面,是则向页面添加日志类别
        if (page.equals(WRITEBLOG)) {
            //向添加日志类别
            List<BlogType> blogTypeCountList = this.blogTypeService.getBlogTypeCountList();
            model.addAttribute("blogTypeCountList", blogTypeCountList);
        }
        if (isUeditor != null) {
            model.addAttribute("isUeditor", isUeditor);
        }
        return "admin/" + page;
    }

    /**
     * 日志修改页面跳转，附带日志id信息
     *
     * @param blogId 要修改的日志id
     * @param model  页面视图模型
     * @return 页面
     */
    @GetMapping("/page/modifyBlog/{blogId}")
    public String forwordModifyBlogPage(@PathVariable(value = "blogId", required = true) Integer blogId, Model model, @RequestParam(value = "isUeditor") Boolean isUeditor) {
        List<BlogType> blogTypeCountList = this.blogTypeService.getBlogTypeCountList();
        model.addAttribute("blogTypeCountList", blogTypeCountList);
        model.addAttribute("blogId", blogId);
        model.addAttribute("isUeditor", isUeditor);
        if (!isUeditor){
            Blog blog = blogService.findBlogById(blogId);
            model.addAttribute("blog", blog);
        }
        return "admin/modifyBlog";
    }

}
