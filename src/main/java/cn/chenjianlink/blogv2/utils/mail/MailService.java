package cn.chenjianlink.blogv2.utils.mail;

import javax.mail.MessagingException;

/**
 * 邮件服务类
 *
 * @author chenjian
 */
public interface MailService {
    /**
     * 发送HTML邮件
     *
     * @param subject 主题
     * @param content 邮件内容
     * @throws MessagingException
     */
    void sentHtmlMail(String subject, String content) throws MessagingException;

}
