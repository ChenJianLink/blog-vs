package cn.chenjianlink.blogv2.mapper;

import cn.chenjianlink.blogv2.pojo.Message;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 留言mapper
 *
 * @author chenjian
 */
@Repository
public interface MessageMapper {
    /**
     * 前台留言查询
     *
     * @return 留言列表
     */
    List<Message> selectSomeList();

    /**
     * 后台
     *
     * @param messageMap 留言状态(后台查询所有留言)
     * @return 留言列表
     */
    List<Message> selectList(Map<String, Integer> messageMap);

    /**
     * 删除留言
     *
     * @param ids 要删除的留言id数组
     */
    void delete(int[] ids);

    /**
     * 修改留言状态为审核通过
     *
     * @param ids 留言id数组
     */
    void updateStateAsAdopt(int[] ids);

    /**
     * 修改留言状态为审核不通过
     *
     * @param ids 留言id数组
     */
    void updateStateAsFail(int[] ids);

    /**
     * 插入新的留言（未审核）
     *
     * @param message 要插入的留言对象
     */
    void insert(Message message);

    /**
     * 统计通过审核的留言总数
     *
     * @return 总数
     */
    int selectAdoptCount();

    /**
     * 根据留言id查询留言
     *
     * @param id 留言id
     * @return 留言
     */
    Message selectByPrimaryKey(int id);

    /**
     * 插入新留言回复
     *
     * @param message 留言回复
     */
    void insertReply(Message message);

    /**
     * 更新留言回复
     *
     * @param message 留言回复
     */
    void updateReply(Message message);

    /**
     * 删除留言回复
     *
     * @param id 留言id
     */
    void deleteReply(Integer id);

}
