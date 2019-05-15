package cn.chenjianlink.blogv2.exception.comment;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

/**
 * 评论异常
 *
 * @author chenjian
 */
public class CommentException extends BlogSystemException {
    public CommentException(String message) {
        super(message);
    }

    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }
}
