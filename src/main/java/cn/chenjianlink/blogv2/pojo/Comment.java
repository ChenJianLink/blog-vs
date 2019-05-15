package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 游客评论相关对象
 *
 * @author chenjian
 */
@Setter
@Getter
public class Comment implements Serializable {
    /**
     * 评论id
     */
    private Integer id;
    /**
     * 评论者ip
     */
    private String userIp;
    /**
     * 评论者名称
     */
    private String userName;
    /**
     * 日志
     */
    private Blog blog;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论日期
     */
    private Date commentDate;
    /**
     * 评论状态
     * 0:待审核，1：审核通过，2：审核不通过
     */
    private Integer state;

}
