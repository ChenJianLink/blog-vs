package cn.chenjianlink.blogv2.mapper;


import cn.chenjianlink.blogv2.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 评论管理mapper
 *
 * @author chenjian
 */
@Repository
public interface CommentMapper {
    /**
     * 查询所有评论
     *
     * @param commentMap 封装查询条件的map
     * @return 评论列表
     */
    List<Comment> selectList(Map<String, Object> commentMap);

    /**
     * 根据日志id返回对应的评论列表
     *
     * @param blogId 日志id
     * @return 评论列表
     */
    List<Comment> selectCommentByBlogId(int blogId);

    /**
     * 删除评论
     *
     * @param ids 要删除的id数组
     */
    void delete(int[] ids);

    /**
     * 根据博客id查询该博客的评论总数
     *
     * @param blogId 日志id
     */
    void selectCommentCount(int blogId);

    /**
     * 修改评论状态为审核通过
     *
     * @param ids 审核通过的评论id数组
     */
    void updateStateAsAdopt(int[] ids);

    /**
     * 修改评论状态为审核不通过
     *
     * @param ids 审核不通过的评论id数组
     */
    void updateStateAsFail(int[] ids);

    /**
     * 插入新的评论（未审核）
     *
     * @param comment 评论对象
     */
    void insert(Comment comment);
}
