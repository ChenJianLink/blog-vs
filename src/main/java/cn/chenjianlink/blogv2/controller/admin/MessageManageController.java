package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.message.MessageException;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.pojo.Message;
import cn.chenjianlink.blogv2.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 留言管理Controller
 *
 * @author chenjian
 */
@RestController
@RequestMapping("/admin/messageManage")
public class MessageManageController {

    @Resource
    private MessageService messageService;

    /**
     * 显示留言列表（留言管理以及留言审核）
     *
     * @param page  请求页面页码
     * @param rows  每页请求数据数
     * @param state 评论状态
     * @return EasyUi响应
     */
    @PostMapping("/message/list")
    public EasyUiResult getMessageList(Integer page, Integer rows, @RequestParam(value = "state", required = false) Integer state) {
        EasyUiResult result = messageService.findMessageListAll(page, rows, state);
        return result;
    }

    /**
     * 删除留言
     *
     * @param ids 要删除的留言id数组
     */
    @DeleteMapping("/message/{ids}")
    public void deleteMessageById(@PathVariable(value = "ids") Integer[] ids) {
        messageService.deleteMessageById(ids);
    }

    /**
     * 审核留言
     *
     * @param ids   要审核的评论id序列
     * @param state 审核状态
     * @throws MessageException 留言异常
     */
    @PutMapping(value = "/message/{ids}")
    public void reviewComment(@PathVariable(value = "ids") Integer[] ids, @RequestParam(value = "state") Integer state) throws MessageException {
        messageService.updateMessageState(ids, state);
    }

    /**
     * 单条留言查询
     *
     * @param id 留言id
     * @return 留言
     */
    @GetMapping(value = "/message/{id}")
    public Message previewMessage(@PathVariable(value = "id") Integer id) {
        Message message = messageService.findMessageById(id);
        return message;
    }

    /**
     * 添加/更新回复内容
     *
     * @param message 封装的回复
     */
    @PostMapping(value = "/message/reply")
    public void reply(Message message) {
        Message oldMessage = messageService.findMessageById(message.getId());
        if (oldMessage.getReply() != null) {
            messageService.updateReply(message);
        } else {
            messageService.addReply(message);
        }
    }
}
