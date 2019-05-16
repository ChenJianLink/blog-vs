package cn.chenjianlink.blogv2.exception.blog;

/**
 * 日志不存在异常
 *
 * @author chenjian
 */
public class BlogNotFoundException extends BlogException {
    public BlogNotFoundException(String message) {
        super(message);
    }

    public BlogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
