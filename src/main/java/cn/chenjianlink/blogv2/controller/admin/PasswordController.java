package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.controller.ControllerMethod;
import cn.chenjianlink.blogv2.pojo.Blogger;
import cn.chenjianlink.blogv2.service.BloggerService;
import cn.chenjianlink.blogv2.utils.BlogResult;
import cn.chenjianlink.blogv2.utils.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 密码管理类
 * 忘记密码，以及通过邮件修改密码
 *
 * @author chenjian
 */
@Slf4j
@Controller
@RequestMapping("/blogger/admin")
public class PasswordController {

    private final transient ReentrantLock reentrantLock = new ReentrantLock();

    @Resource
    private MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

    private static final transient ConcurrentHashMap<String, String> CHECKMAP = new ConcurrentHashMap<>(4);

    private static final String URL = "url";

    private static final String SESSIONKEY = "modifyPasswordKey";

    @Resource
    private BloggerService bloggerService;

    /**
     * 忘记密码
     *
     * @return 成功信息
     */
    @PostMapping("/forgetPassword")
    @ResponseBody
    public BlogResult sendModifyPasswordEmail(HttpServletRequest request) {
        ReentrantLock lock = this.reentrantLock;
        lock.lock();
        try {
            //放置于session中的验证信息
            String sessionKey = UUID.randomUUID().toString();
            //修改页面url（随机生成）
            String url = UUID.randomUUID().toString();
            HttpSession session = request.getSession();
            session.setAttribute("modifyPasswordKey", sessionKey);
            session.setMaxInactiveInterval(10 * 60);
            Context context = new Context();
            context.setVariable(URL, url);
            String templateMail = templateEngine.process("admin/modifyMailTemplate", context);
            mailService.sentHtmlMail("修改局外人的密码", templateMail);
            CHECKMAP.put(URL, url);
            CHECKMAP.put(SESSIONKEY, sessionKey);
            return BlogResult.ok();
        } catch (MessagingException e) {
            log.error("修改密码邮件发送异常:" + e.getMessage(), e);
            return BlogResult.showError("系统异常");
        } finally {
            lock.unlock();
        }
    }

    /**
     * 密码修改页面展示
     *
     * @return 成功信息
     */
    @PostMapping("/{url}")
    public String showModifyPasswordPage(@PathVariable(value = "url") String url, HttpServletRequest request) {
        ReentrantLock lock = this.reentrantLock;
        lock.lock();
        try {
            HttpSession session = request.getSession();
            String sessionKey = (String) session.getAttribute(SESSIONKEY);
            //判断url和sessionKey是否合法
            if (url.equals(CHECKMAP.get(URL)) && sessionKey != null && sessionKey.equals(CHECKMAP.get(SESSIONKEY))) {
                return "admin/modifyPasswoedPage";
            }
            return "error/4xx";
        } finally {
            lock.unlock();
        }
    }

    /**
     * 修改密码
     *
     * @param request  请求
     * @param password 密码
     * @return 自定义相应
     */
    @PutMapping("/modifyPassword")
    @ResponseBody
    public BlogResult modifyPassword(HttpServletRequest request, @RequestParam(value = "newPassword") String password) {
        ReentrantLock lock = this.reentrantLock;
        lock.lock();
        try {
            HttpSession session = request.getSession();
            String sessionKey = (String) session.getAttribute(SESSIONKEY);
            if (sessionKey == null && !sessionKey.equals(CHECKMAP.get(SESSIONKEY))) {
                CHECKMAP.clear();
                session.removeAttribute(SESSIONKEY);
                return BlogResult.showError("修改超时，请重新操作");
            }
            //及时移除url和sessionKey
            CHECKMAP.clear();
            session.removeAttribute(SESSIONKEY);
            //修改密码
            Blogger blogger = new Blogger();
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
            String newPassword = ControllerMethod.encrypt(password, salt);
            blogger.setPassword(newPassword);
            blogger.setSalt(salt);
            this.bloggerService.updatePassword(blogger);
            return BlogResult.ok();
        } finally {
            lock.unlock();
        }
    }
}
