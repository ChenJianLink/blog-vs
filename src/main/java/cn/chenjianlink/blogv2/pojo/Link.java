package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 友情链接对象
 *
 * @author chenjian
 */
@Setter
@Getter
@ToString
public class Link implements Serializable {
    private Integer id;
    /**
     * 链接名称
     */
    private String linkName;
    /**
     * 链接路径
     */
    private String linkUrl;
    /**
     * 排序序号
     */
    private Integer orderNo;

}
