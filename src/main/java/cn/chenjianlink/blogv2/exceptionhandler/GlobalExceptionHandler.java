package cn.chenjianlink.blogv2.exceptionhandler;

import cn.chenjianlink.blogv2.exception.BlogSystemException;
import cn.chenjianlink.blogv2.exception.blog.BlogException;
import cn.chenjianlink.blogv2.exception.blog.BlogNotFoundException;
import cn.chenjianlink.blogv2.exception.blogger.BloggerException;
import cn.chenjianlink.blogv2.exception.blogtype.BlogTypeException;
import cn.chenjianlink.blogv2.exception.comment.CommentException;
import cn.chenjianlink.blogv2.exception.link.LinkException;
import cn.chenjianlink.blogv2.exception.message.MessageException;
import cn.chenjianlink.blogv2.exception.other.IpAddressQueryException;
import cn.chenjianlink.blogv2.exception.other.UeditorInitializeException;
import cn.chenjianlink.blogv2.utils.BlogResult;
import cn.chenjianlink.blogv2.utils.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;


/**
 * 全局异常处理器
 *
 * @author chenjian
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private MailService mailService;

    /**
     * 处理运行时异常
     *
     * @param e 运行时异常
     * @return 异常信息
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BlogResult runtimeExceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        this.sendErrorMail(e);
        BlogResult result = new BlogResult();
        result.setErrorInfo("系统异常");
        result.setStatus(500);
        result.setTimes(new Date());
        return result;
    }

    /**
     * 处理日志异常
     *
     * @param e 日志异常
     * @return 异常信息
     */
    @ExceptionHandler(BlogException.class)
    public ModelAndView blogExceptionHandler(BlogException e) {
        log.error(e.getMessage(), e);
        BlogResult result = new BlogResult();
        ModelAndView modelAndView = new ModelAndView();
        result.setErrorInfo("系统异常");
        result.setTimes(new Date());
        modelAndView.addObject(result);
        if (e instanceof BlogNotFoundException) {
            result.setStatus(404);
            modelAndView.setViewName("error/4xx.html");
            return modelAndView;
        }
        result.setStatus(500);
        this.sendErrorMail(e);
        return modelAndView;
    }

    /**
     * 其他异常
     *
     * @param e 系统异常
     * @return 异常信息
     */
    @ExceptionHandler({MessageException.class, BloggerException.class, UeditorInitializeException.class, LinkException.class, BlogTypeException.class, CommentException.class, IpAddressQueryException.class})
    @ResponseBody
    public BlogResult blogSystemExceptionHandler(BlogSystemException e) {
        log.error(e.getMessage(), e);
        this.sendErrorMail(e);
        BlogResult result = new BlogResult();
        result.setErrorInfo("系统异常");
        result.setTimes(new Date());
        result.setStatus(500);
        return result;
    }

    /**
     * 向站长邮箱发送异常信息
     *
     * @param e 异常
     */
    private void sendErrorMail(Exception e) {
        Context context = new Context();
        context.setVariable("message", e.toString());
        String templateMail = templateEngine.process("error/errorMailTemplate", context);
        try {
            mailService.sentHtmlMail("局外人日志系统异常", templateMail);
        } catch (MessagingException ex) {
            log.error("异常邮件发送失败:", ex);
        }
    }
}
