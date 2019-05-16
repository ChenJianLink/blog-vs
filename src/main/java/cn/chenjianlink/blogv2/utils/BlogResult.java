package cn.chenjianlink.blogv2.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志自定义响应结构
 *
 * @author chenjian
 */
@Getter
@Setter
public class BlogResult implements Serializable {


    /**
     * 异常信息
     */
    private String errorInfo;

    private Date times;

    /**
     * 200,4xx,5xx
     */
    private Integer status;

    public BlogResult() {
    }

    public BlogResult(Integer status) {
        this.status = status;
        this.times = new Date();
    }

    public BlogResult(Integer status, String errorInfo) {
        this.status = status;
        this.errorInfo = errorInfo;
        this.times = new Date();
    }

    /**
     * 业务成功执行
     *
     * @return 自定义响应
     */
    public static BlogResult ok() {
        return new BlogResult(200);
    }

    /**
     * 展示错误信息
     *
     * @param errorInfo 错误信息
     * @return 封装错误信息的自定义响应
     */
    public static BlogResult showError(String errorInfo) {
        return new BlogResult(200, errorInfo);
    }

}
