package cn.chenjianlink.blogv2.mapper;

import cn.chenjianlink.blogv2.pojo.BlogType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 日志类别mapper
 *
 * @author chenjian
 */
@Repository
public interface BlogTypeMapper {
    /**
     * 查询所有日志类别以及该类别下的日志数量
     *
     * @return 日志类别列表（不包含排序号）
     */
    List<BlogType> selectAll();

    /**
     * 查询所有日志类别包括日志数量
     *
     * @return 日志类别列表（包含排序号）
     */
    List<BlogType> selectList();

    /**
     * 添加日志类别
     *
     * @param blogType 要新增的日志类别对象
     */
    void insert(BlogType blogType);

    /**
     * 更新日志类别
     *
     * @param blogType 要更新的日志类别对象
     */
    void update(BlogType blogType);

    /**
     * 根据id查询日志类别
     *
     * @param id 要查询的日志类别id
     * @return id对应的日志类别
     */
    BlogType selectByPrimaryKey(int id);

    /**
     * 删除日志类别
     *
     * @param ids 要删除的日志类别id数组
     */
    void delete(int[] ids);

    /**
     * 根据id查询所有日志类别以及该类别下的日志数量
     *
     * @param ids 要查询日志类别id数组
     * @return id数组对应的日志类别(以List形式)
     */
    List<BlogType> selectTypeCount(int[] ids);
}
