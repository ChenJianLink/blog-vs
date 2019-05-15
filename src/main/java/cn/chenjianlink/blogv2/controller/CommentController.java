package cn.chenjianlink.blogv2.controller;

import cn.chenjianlink.blogv2.pojo.Comment;
import cn.chenjianlink.blogv2.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 评论发表Controller
 *
 * @author chenjian
 */
@RestController
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 发表评论
     *
     * @param comment 评论对象
     * @param request 请求
     */
    @PostMapping(value = "/comment/leave")
    public void comment(Comment comment, HttpServletRequest request) {
        String userIp = request.getRemoteAddr();
        comment.setUserIp(userIp);
        commentService.addComment(comment);
    }
}
