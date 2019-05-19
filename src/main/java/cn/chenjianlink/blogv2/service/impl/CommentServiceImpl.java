package cn.chenjianlink.blogv2.service.impl;

import cn.chenjianlink.blogv2.exception.comment.CommentException;
import cn.chenjianlink.blogv2.mapper.CommentMapper;
import cn.chenjianlink.blogv2.pojo.Comment;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 评论管理service
 *
 * @author chenjian
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    public static final int FAIL = 2;

    /**
     * 评论审核查询待审核评论
     */
    @Override
    @Cacheable(value = "commentCache")
    public EasyUiResult findCommentList(Integer page, Integer rows, Map<String, Object> commentMap) {
        PageHelper.startPage(page, rows);
        List<Comment> commentList = commentMapper.selectList(commentMap);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        long total = pageInfo.getTotal();
        EasyUiResult result = new EasyUiResult(total, commentList);
        return result;
    }

    /**
     * 删除评论
     */
    @Override
    @CacheEvict(value = {"commentCache", "blogCache"}, allEntries = true)
    public void deleteCommentById(Integer[] ids) {
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        commentMapper.delete(id);
    }

    /**
     * 审核评论
     */
    @Override
    @CacheEvict(value = {"commentCache", "blogCache"}, allEntries = true)
    public void updateCommentState(Integer[] ids, Integer state) throws CommentException {
        //数组类型转换
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        //判断state
        if (state == 1) {
            commentMapper.updateStateAsAdopt(id);
        } else if (state == FAIL) {
            commentMapper.updateStateAsFail(id);
        } else {
            throw new CommentException("非法的请求：state-->" + state + ",不存在这类状态");
        }
    }

    /**
     * 前台日志展示相关评论
     */
    @Override
    @Cacheable(value = "commentCache")
    public List<Comment> findCommentListByBlogId(Integer blogId) {
        List<Comment> commentList = commentMapper.selectCommentByBlogId(blogId);
        return commentList;
    }

    /**
     * 增加新评论
     */
    @Override
    @CacheEvict(value = "commentCache", allEntries = true)
    public void addComment(Comment comment) {
        comment.setCommentDate(new Date());
        commentMapper.insert(comment);
    }
}
