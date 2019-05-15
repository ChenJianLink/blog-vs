package cn.chenjianlink.blogv2.controller;

import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.BlogType;
import cn.chenjianlink.blogv2.pojo.Blogger;
import cn.chenjianlink.blogv2.pojo.Link;
import cn.chenjianlink.blogv2.service.BlogService;
import cn.chenjianlink.blogv2.service.BlogTypeService;
import cn.chenjianlink.blogv2.service.BloggerService;
import cn.chenjianlink.blogv2.service.LinkService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 封装controller常用代码
 *
 * @author chenjian
 */
@Component
public class ControllerMethod {

    private static LinkService linkService;

    private static BloggerService bloggerService;

    private static BlogTypeService blogTypeService;

    private static BlogService blogService;

    private static String ALGORITHMNAME;

    private static Integer ITERATIONS;

    private static final String PAGE = "&page=";

    @Resource
    public void setLinkService(LinkService linkService) {
        ControllerMethod.linkService = linkService;
    }

    @Resource
    public void setBloggerService(BloggerService bloggerService) {
        ControllerMethod.bloggerService = bloggerService;
    }

    @Resource
    public void setBlogTypeService(BlogTypeService blogTypeService) {
        ControllerMethod.blogTypeService = blogTypeService;
    }

    @Resource
    public void setBlogService(BlogService blogService) {
        ControllerMethod.blogService = blogService;
    }

    @Value("${blog.algorithmName}")
    public void setALGORITHMNAME(String algorithmName) {
        ControllerMethod.ALGORITHMNAME = algorithmName;
    }

    @Value("${blog.iterations}")
    public void setITERATIONS(Integer iterations) {
        ControllerMethod.ITERATIONS = iterations;
    }

    /**
     * 显示主页的友情链接，Master信息，日志分类的代码
     *
     * @param model 页面视图模型
     */
    public static void showMainTemp(Model model) {
        //Master信息查询
        Blogger blogger = bloggerService.findBlogger();
        model.addAttribute("blogger", blogger);
        //友情链接查询
        List<Link> linkList = linkService.getLinkList();
        model.addAttribute("linkList", linkList);
        //日志类型查询
        List<BlogType> blogTypeList = blogTypeService.getBlogTypeCountList();
        model.addAttribute("blogTypeCountList", blogTypeList);
        //根据发布日期查询
        List<Blog> blogList = blogService.findBlogDateList();
        model.addAttribute("blogCountList", blogList);
    }

    /**
     * 获取请求的url,与分页信息相关
     *
     * @param request 请求
     * @return 请求url
     */
    public static String getUrl(HttpServletRequest request) {
        //获取项目名
        String contextPath = request.getContextPath();
        //获取servlet
        String servletPath = request.getServletPath();
        //获取问号后的参数
        String queryString = request.getQueryString();
        //判断参数是否为空
        if (queryString != null) {
            //判断参数部分是否带page
            if (queryString.contains(PAGE)) {
                int index = queryString.lastIndexOf("&page=");
                queryString = queryString.substring(0, index);
            }
        } else {
            //设置为空串
            queryString = "";
        }
        //url拼接
        return contextPath + servletPath + "?" + queryString;
    }

    /**
     * 加密
     *
     * @param password 密码
     * @param salt     颜值
     * @return 加密后的密码
     */
    public static String encrypt(String password, String salt) {
        String newPassword = new SimpleHash(ALGORITHMNAME, password, salt, ITERATIONS).toHex();
        return newPassword;
    }
}
