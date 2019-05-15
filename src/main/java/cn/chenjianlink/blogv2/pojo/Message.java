package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 留言对象
 *
 * @author chenjian
 */
@Setter
@Getter
public class Message implements Serializable {
    private Integer id;
    /**
     * 留言者ip
     */
    private String userIp;
    /**
     * 留言者名称
     */
    private String userName;
    /**
     * 留言内容
     */
    private String content;
    /**
     * 留言时间
     */
    private Date leaveMessageDate;
    /**
     * 留言状态
     * 0:待审核，1：审核通过，2：审核不通过
     */
    private Integer state;

}
