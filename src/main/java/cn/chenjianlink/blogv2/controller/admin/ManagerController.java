package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.pojo.Blogger;
import cn.chenjianlink.blogv2.utils.BlogResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 后台用户登录退出Controller
 *
 * @author chenjian
 */
@Controller
@RequestMapping("/admin/blogger")
public class ManagerController {

    /**
     * 显示登录页面
     *
     * @return 登录页面
     */
    @GetMapping("/admin-login")
    public String showLoginPage() {
        return "admin/login";
    }

    @Value("${blog.sessionTimeOut}")
    private long sessionTimeOut;

    /**
     * 登录
     *
     * @param blogger 登录用户对象
     * @return 响应结果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public BlogResult login(Blogger blogger) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(blogger.getUserName(), blogger.getPassword());
        try {
            subject.login(token);
            subject.getSession().setTimeout(sessionTimeOut);
            return BlogResult.ok();
        } catch (AuthenticationException | InvalidSessionException e) {
            return BlogResult.showError("用户名或密码不正确");
        }
    }

    /**
     * 后台首页展示
     *
     * @return 后台首页
     */
    @GetMapping("/admin-index")
    public String adminIndex() {
        return "admin/main";
    }

    /**
     * 退出
     *
     * @return 登录页面
     */
    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:admin-login";
    }

}
