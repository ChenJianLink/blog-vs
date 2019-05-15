package cn.chenjianlink.blogv2.exception.blog;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * 日志异常类
 *
 * @author chenjian
 */
public class BlogException extends BlogSystemException {

    public BlogException(String message) {
        super(message);
    }

    public BlogException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
