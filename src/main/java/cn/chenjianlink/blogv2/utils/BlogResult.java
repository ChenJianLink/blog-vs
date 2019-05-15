package cn.chenjianlink.blogv2.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 日志自定义响应结构
 *
 * @author chenjian
 */
public class BlogResult implements Serializable {


    /**
     * 异常信息
     */
    @Getter
    @Setter
    private String errorInfo;

    public BlogResult() {

    }

    public BlogResult(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    /**
     * 业务成功执行
     *
     * @return 自定义响应
     */
    public static BlogResult ok() {
        return new BlogResult();
    }

    /**
     * 展示错误信息
     *
     * @param errorInfo 错误信息
     * @return 封装错误信息的自定义响应
     */
    public static BlogResult showError(String errorInfo) {
        return new BlogResult(errorInfo);
    }

}
