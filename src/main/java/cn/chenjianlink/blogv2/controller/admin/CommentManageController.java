package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.comment.CommentException;
import cn.chenjianlink.blogv2.pojo.Comment;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 评论管理Controller
 *
 * @author chenjian
 */
@RestController
@RequestMapping("/admin/commentManage")
public class CommentManageController {

    @Resource
    private CommentService commentService;

    /**
     * 显示评论列表（评论管理以及评论审核）
     *
     * @param page  请求页面页码
     * @param rows  每页请求数据数
     * @param state 评论状态
     * @return EasyUi响应
     */
    @PostMapping("/comment/list")
    public EasyUiResult getCommentList(Integer page, Integer rows, @RequestParam(value = "state", required = false) Integer state) {
        Map<String, Object> commentMap = new HashMap<>(3);
        commentMap.put("state", state);
        commentMap.put("blogId", null);
        EasyUiResult commentList = commentService.findCommentList(page, rows, commentMap);
        return commentList;
    }

    /**
     * 删除评论
     *
     * @param ids 要删除的评论id数组
     */
    @DeleteMapping("/comment/{ids}")
    public void deleteCommentById(@PathVariable(value = "ids", required = true) Integer[] ids) {
        commentService.deleteCommentById(ids);
    }

    /**
     * 审核评论
     *
     * @param ids   要审核的评论id序列
     * @param state 审核状态
     * @throws CommentException 评论异常
     */
    @PutMapping("/comment/{ids}")
    public void reviewComment(@PathVariable(value = "ids", required = true) Integer[] ids, @RequestParam(value = "state", required = true) Integer state) throws CommentException {
        commentService.updateCommentState(ids, state);
    }

    /**
     * 单条留言查询
     *
     * @param id 留言id
     * @return 留言
     */
    @GetMapping(value = "/comment/{id}")
    public Comment previewComment(@PathVariable(value = "id") Integer id) {
        Comment comment = commentService.findCommentById(id);
        return comment;
    }

    /**
     * 添加/更新回复内容
     *
     * @param comment 封装的回复
     */
    @PostMapping(value = "/comment/reply")
    public void reply(Comment comment) {
        Comment oldComment = commentService.findCommentById(comment.getId());
        if (oldComment.getReply() != null) {
            commentService.updateReply(comment);
        } else {
            commentService.addReply(comment);
        }
    }
}
