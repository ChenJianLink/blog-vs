package cn.chenjianlink.blogv2.exception;

/**
 * 日志系统异常（系统最高级异常）所有异常都继承该异常
 *
 * @author chenjian
 */
public class BlogSystemException extends Exception {
    public BlogSystemException() {
        super();
    }

    public BlogSystemException(String message) {
        super(message);
    }

    public BlogSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
