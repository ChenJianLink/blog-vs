package cn.chenjianlink.blogv2.mapper;


import cn.chenjianlink.blogv2.pojo.Link;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 友情链接mapper
 *
 * @author chenjian
 */
@Repository
public interface LinkMapper {
    /**
     * 查询链接全部信息
     *
     * @return 友情链接列表(包含id)
     */
    List<Link> selectAll();

    /**
     * 查询全部链接
     *
     * @return 友情链接列表
     */
    List<Link> selectList();

    /**
     * 插入新链接
     *
     * @param link 友情链接对象
     */
    void insert(Link link);

    /**
     * 根据id查询链接
     *
     * @param id 友情链接id
     * @return 友情链接对象
     */
    Link selectByPrimaryKey(int id);

    /**
     * 更新链接
     *
     * @param link 更新的友情链接对象
     */
    void update(Link link);

    /**
     * 删除链接
     *
     * @param ids 要删除的友情链接id数组
     */
    void delete(int[] ids);
}
