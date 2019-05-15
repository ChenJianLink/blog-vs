package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 日志类型对象
 *
 * @author chenjian
 */
@Setter
@Getter
@ToString
public class BlogType implements Serializable {
    private Integer id;
    /**
     * 类别名称
     */
    private String typeName;
    /**
     * 排序序号
     */
    private Integer orderNo;
    /**
     * 分类下日志数量
     */
    private Integer blogCount;

}
