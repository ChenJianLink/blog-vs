package cn.chenjianlink.blogv2.exception.blogger;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * 站长异常
 *
 * @author chenjian
 */
public class BloggerException extends BlogSystemException {


    public BloggerException(String message) {
        super(message);
    }

    public BloggerException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
