package cn.chenjianlink.blogv2.pojo;


import lombok.Getter;
import lombok.Setter;

/**
 * Ip相关pojo
 *
 * @author chenjian
 */
@Setter
@Getter
public class Ip {
    /**
     * 请求时间
     */
    private Long time;
    /**
     * 请求的ip
     */
    private String ip;
    /**
     * 请求次数
     */
    private Integer recount;

}
