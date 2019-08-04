package cn.chenjianlink.blogv2.exception.other;

import cn.chenjianlink.blogv2.exception.BlogSystemException;

public class IpAddressQueryException extends BlogSystemException {
    public IpAddressQueryException() {
        super();
    }

    public IpAddressQueryException(String message) {
        super(message);
    }

    public IpAddressQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
