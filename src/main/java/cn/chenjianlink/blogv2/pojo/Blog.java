package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 日志内容对象
 *
 * @author chenjian
 */
@Setter
@Getter
@ToString
public class Blog implements Serializable {
    /**
     * 日志id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 日志摘要
     */
    private String summary;
    /**
     * 发表日期
     */
    private Date releaseDate;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 按发表日期分类的日期
     */
    private String releaseDateStr;
    /**
     * 按发表日期分类的日志总数
     */
    private Integer blogCount;
    /**
     * 点击量
     */
    private Integer clickHit;
    /**
     * 评论量
     */
    private Integer replyHit;
    /**
     * 日志内容
     */
    private String content;
    /**
     * markdown文本
     */
    private String mdContent;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 日志状态
     */
    private Integer state;
    /**
     * 日志类别
     */
    private BlogType blogType;
    /**
     * 日志中的图片
     */
    private List<String> imagesList = new LinkedList<>();
    /**
     * 用于查询的日志内容
     */
    private String searchContent;
    /**
     * 编辑器判断位
     */
    private Boolean isUeditor;

}
