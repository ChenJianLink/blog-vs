package cn.chenjianlink.blogv2.service;

import cn.chenjianlink.blogv2.exception.message.MessageException;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.pojo.Message;
import cn.chenjianlink.blogv2.utils.BlogResult;

import java.util.List;

/**
 * 留言service
 *
 * @author chenjian
 */
public interface MessageService {
    /**
     * 留言管理列表显示
     *
     * @param page  要请求页面页码
     * @param rows  每页要查询的记录数
     * @param state 留言状态（可为空）
     * @return 封装查询信息的EasyUI响应类
     */
    EasyUiResult findMessageListAll(Integer page, Integer rows, Integer state);

    /**
     * 删除留言
     *
     * @param ids 要删除的留言id数组
     */
    void deleteMessageById(Integer[] ids);

    /**
     * 审核留言
     *
     * @param ids   批量审核留言的留言id数组
     * @param state 审核状态
     */
    void updateMessageState(Integer[] ids, Integer state) throws MessageException;

    /**
     * 显示留言
     *
     * @return 留言列表(前台使用)
     */
    List<Message> findMessageList();

    /**
     * 增加新留言
     *
     * @param message 新增的留言对象
     */
    void addMessage(Message message);
}
