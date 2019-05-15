package cn.chenjianlink.blogv2.exception.link;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * 友情链接异常
 *
 * @author chenjian
 */
public class LinkException extends BlogSystemException {
    public LinkException(String message) {
        super(message);
    }

    public LinkException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
