package cn.chenjianlink.blogv2.exception.other;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * ueditor初始化异常
 *
 * @author chenjian
 */
public class UeditorInitializeException extends BlogSystemException {

    public UeditorInitializeException(String message) {
        super(message);
    }

    public UeditorInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
