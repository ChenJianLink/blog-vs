package cn.chenjianlink.blogv2.exception.message;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * 留言异常
 *
 * @author chenjian
 */
public class MessageException extends BlogSystemException {
    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
