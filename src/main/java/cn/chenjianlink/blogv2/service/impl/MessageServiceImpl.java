package cn.chenjianlink.blogv2.service.impl;

import cn.chenjianlink.blogv2.exception.message.MessageException;
import cn.chenjianlink.blogv2.mapper.MessageMapper;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.pojo.Message;
import cn.chenjianlink.blogv2.service.MessageService;
import cn.chenjianlink.blogv2.utils.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 留言管理service
 *
 * @author chenjian
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Value("${blog.rows}")
    private Integer rows;

    private static final int FAIL = 2;

    private static final int ADOPT = 1;

    /**
     * 留言列表显示
     */
    @Override
    @Cacheable(value = "messageCache")
    public EasyUiResult findMessageListAll(Integer page, Integer rows, Integer state) {
        PageHelper.startPage(page, rows);
        Map<String, Integer> messageMap = new HashMap<>(2);
        messageMap.put("state", state);
        List<Message> messageList = messageMapper.selectList(messageMap);
        PageInfo<Message> pageInfo = new PageInfo<>(messageList);
        long total = pageInfo.getTotal();
        EasyUiResult result = new EasyUiResult(total, messageList);
        return result;
    }

    /**
     * 删除留言
     */
    @Override
    @CacheEvict(value = "messageCache", allEntries = true)
    public void deleteMessageById(Integer[] ids) {
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        messageMapper.delete(id);
    }

    /**
     * 更新留言状态
     */
    @Override
    @CacheEvict(value = "messageCache", allEntries = true)
    public void updateMessageState(Integer[] ids, Integer state) throws MessageException {
        //数组类型转换
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        //判断state
        if (state == ADOPT) {
            messageMapper.updateStateAsAdopt(id);
        } else if (state == FAIL) {
            messageMapper.updateStateAsFail(id);
        } else {
            throw new MessageException("非法的请求：state-->" + state + ",不存在这类状态");
        }
    }

    /**
     * 留言显示
     */
    @Override
    @Cacheable(value = "messageCache")
    public PageResult findMessageList(Integer page) {
        //对过大的page处理
        int totalRows = this.messageMapper.selectAdoptCount();
        int totalPage = totalRows / (2 * rows);
        totalPage = totalRows % (2 * rows) == 0 ? totalPage : totalPage + 1;
        page = page <= totalPage ? page : totalPage;
        //设置分页信息
        PageHelper.startPage(page, 2 * rows);
        Map<String, Integer> messageMap = new HashMap<>(2);
        messageMap.put("state", ADOPT);
        List<Message> messageList = messageMapper.selectList(messageMap);
        PageResult result = new PageResult(page, totalRows, 2 * rows, messageList);
        return result;
    }

    /**
     * 添加留言
     */
    @Override
    @CacheEvict(value = "messageCache", allEntries = true)
    public void addMessage(Message message) {
        message.setLeaveMessageDate(new Date());
        messageMapper.insert(message);
    }
}
