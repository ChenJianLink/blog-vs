package cn.chenjianlink.blogv2.service;

import cn.chenjianlink.blogv2.exception.comment.CommentException;
import cn.chenjianlink.blogv2.pojo.Comment;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;

import java.util.List;
import java.util.Map;

/**
 * 评论service
 *
 * @author chenjian
 */
public interface CommentService {
    /**
     * 评论列表显示(map为空则后台使用，map不为空则首页使用)
     *
     * @param page       要请求页面页码
     * @param rows       每页要查询的记录数
     * @param commentMap 封装查询状态state的map
     * @return 封装查询结果的EasyUI响应类
     */
    EasyUiResult findCommentList(Integer page, Integer rows, Map<String, Object> commentMap);

    /**
     * 删除评论
     *
     * @param ids 要删除的评论的id数组
     */
    void deleteCommentById(Integer[] ids);

    /**
     * 审核评论
     *
     * @param ids   要批量审核的评论id数组
     * @param state 审核状态(1.通过，2.不通过)
     * @throws CommentException 评论异常
     */
    void updateCommentState(Integer[] ids, Integer state) throws CommentException;

    /**
     * 显示评论
     *
     * @param blogId 日志id
     * @return 在该日志下的所有评论信息
     */
    List<Comment> findCommentListByBlogId(Integer blogId);

    /**
     * 增加新评论
     *
     * @param comment 新增的评论对象
     */
    void addComment(Comment comment);
}
