package cn.chenjianlink.blogv2.utils.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * MailService实现类
 *
 * @author chenjian
 */
@Service
public class MailServiceImpl implements MailService {

    /**
     * 发送者邮箱地址
     */
    @Value("${spring.mail.username}")
    private String from;

    @Value("${blog.email}")
    private String to;

    /**
     * 邮件发送对象
     */
    @Resource
    private JavaMailSender mailSender;

    @Override
    public void sentHtmlMail(String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setFrom(from);
        mailSender.send(message);
    }
}
