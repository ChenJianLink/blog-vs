package cn.chenjianlink.blogv2.exception.blogtype;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * 日志类别异常
 *
 * @author chenjian
 */
public class BlogTypeException extends BlogSystemException {

    public BlogTypeException(String message) {
        super(message);
    }

    public BlogTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
