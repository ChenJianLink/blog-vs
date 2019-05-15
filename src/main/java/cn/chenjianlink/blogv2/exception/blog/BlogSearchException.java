package cn.chenjianlink.blogv2.exception.blog;

/**
 * 日志查找模块异常类
 *
 * @author chenjian
 */
public class BlogSearchException extends BlogException {
    public BlogSearchException(String message) {
        super(message);
    }

    public BlogSearchException(String message, Throwable cause) {
        super(message, cause);
    }

}
