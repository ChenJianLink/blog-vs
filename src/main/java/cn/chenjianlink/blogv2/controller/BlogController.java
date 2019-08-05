package cn.chenjianlink.blogv2.controller;

import cn.chenjianlink.blogv2.exception.blog.BlogNotFoundException;
import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.Comment;
import cn.chenjianlink.blogv2.utils.PageResult;
import cn.chenjianlink.blogv2.service.BlogService;
import cn.chenjianlink.blogv2.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 日志相关展示Controller
 *
 * @author chenjian
 */
@Controller
public class BlogController {

    @Resource
    private BlogService blogService;
    @Resource
    private CommentService commentService;
    @Resource
    private ControllerMethod controllerMethod;

    private static final int PUBLISH = 2;

    /**
     * 搜索日志
     *
     * @param model   页面视图模型
     * @param query   关键字
     * @param page    要请求页面页码
     * @param request 请求
     * @return 主页面
     * @throws BlogSearchException 日志搜索异常
     */
    @GetMapping("/blog/query")
    public String searchBlog(Model model, @RequestParam(value = "query", required = true) String query, @RequestParam(value = "page", required = false) Integer page, HttpServletRequest request) throws BlogSearchException {
        String url = controllerMethod.getUrl(request);
        /*
         * 若page为空，则设置page为1，同时处理分页跳转参数问题
         */
        if (page == null || page <= 0) {
            page = 1;
            //处理post请求url没带参数导致分页跳转异常
            if (request.getQueryString() == null || request.getQueryString().isEmpty()) {
                url = url + "query=" + query;
            }
        }
        PageResult pageResult = blogService.searchBlogByQuery(page, query.trim());
        pageResult.setUrl(url);
        controllerMethod.showMainTemp(model);
        model.addAttribute("page", pageResult);
        model.addAttribute("query", query);
        model.addAttribute("blogList", pageResult.getPageList());
        model.addAttribute("pageTitle", query + "-局外人搜索");
        model.addAttribute("mainPage", "foreground/blog/result.html");
        return "mainTemp";
    }

    /**
     * 显示日志内容
     *
     * @param model   页面视图模型
     * @param blogId  日志id
     * @param request 请求
     * @return 主页面
     * @throws BlogNotFoundException 日志异常
     */
    @GetMapping("/blog/articles/{blogId}")
    public String showBlogInfo(Model model, @PathVariable(value = "blogId", required = true) Integer blogId, HttpServletRequest request) throws BlogNotFoundException {
        Blog blog = blogService.findBlogById(blogId);
        if (blog == null) {
            throw new BlogNotFoundException("日志不存在");
        } else if (blog.getState() != PUBLISH) {
            //判断日志的状态
            throw new BlogNotFoundException("不支持的日志查询");
        }
        //查询上一篇日志
        Blog preBlog = blogService.findPreBlog(blog);
        //查询下一篇日志
        Blog nextBlog = blogService.findNextBlog(blog);
        //查询所有评论
        List<Comment> commentList = commentService.findCommentListByBlogId(blogId);
        /*
         * 将访客标识写入session，设置session的过期时间为20分钟，在过期时间内再次访问该日志不会增加阅读量
         * 能防止刷新导致阅读量暴增
         */
        String ip = request.getRemoteAddr();
        HttpSession session = request.getSession();
        String ipSign = (String) session.getAttribute(blogId.toString());
        if (ipSign == null || ipSign.isEmpty()) {
            session.setAttribute(blogId.toString(), blogId.toString() + ip);
            session.setMaxInactiveInterval(20 * 60);
            blog.setClickHit(blog.getClickHit() + 1);
            blogService.updateClick(blog);
        }
        //判断关键字是否为空
        String[] keyWords = null;
        if (blog.getKeyWord() != null && !blog.getKeyWord().isEmpty()) {
            keyWords = blog.getKeyWord().trim().split(",");
        }
        controllerMethod.showMainTemp(model);
        model.addAttribute("commentList", commentList);
        model.addAttribute("pre", preBlog);
        model.addAttribute("next", nextBlog);
        model.addAttribute("blog", blog);
        model.addAttribute("keyWords", keyWords);
        model.addAttribute("pageTitle", blog.getTitle() + "-局外人之秘境");
        model.addAttribute("mainPage", "foreground/blog/view.html");
        return "mainTemp";
    }
}
