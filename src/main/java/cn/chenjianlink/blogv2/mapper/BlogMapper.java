package cn.chenjianlink.blogv2.mapper;

import cn.chenjianlink.blogv2.pojo.Blog;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志管理mapper
 *
 * @author chenjian
 */
@Repository
public interface BlogMapper {
    /**
     * 首页日志展示
     *
     * @param blogMap 封装了查询条件的map(发布日期，日志类别)
     * @return 日志列表
     */
    List<Blog> selectListAll(Map<String, Object> blogMap);

    /**
     * 按日志发布日期查询
     *
     * @return 日志列表
     */
    List<Blog> selectCountList();

    /**
     * 后台日志管理列表展示
     *
     * @param title 日志的标题
     * @return 日志列表
     */
    List<Blog> selectList(String title);

    /**
     * 根据id查询日志(前台查询)
     *
     * @param id 要查询的日志的id
     * @return id对应的日志对象
     */
    Blog selectByPrimaryKey(int id);

    /**
     * 根据id查询日志(后台查询)
     *
     * @param id 要查询的日志的id
     * @return id对应的日志对象
     */
    Blog selectForAdminByPrimaryKey(int id);

    /**
     * 由id删除日志
     *
     * @param id 要删除日志对应的id(批量删除，传入id数组)
     */
    void delete(int[] id);

    /**
     * 更新日志
     *
     * @param blog 要更新的日志对象
     */
    void update(Blog blog);

    /**
     * 增加新日志
     *
     * @param blog 要新增的日志对象
     */
    void insert(Blog blog);

    /**
     * 查询日志总数
     *
     * @param blogMap 封装了查询条件的map(发布日期，日志类别)
     * @return 日志总数
     */
    int selectCount(Map<String, Object> blogMap);

    /**
     * 查询上一篇日志
     *
     * @param date 当前日志的发布日期
     * @return 晚于当前日志发布的日志
     */
    Blog selectPre(Date date);

    /**
     * 查询下一篇日志
     *
     * @param date 当前日志的发布日期
     * @return 先于当前日志发布的日志
     */
    Blog selectNext(Date date);

    /**
     * 重新构建日志索引的查询
     *
     * @return state=2的日志（id，标题，内容，发布日期）
     */
    List<Blog> selectBlogForIndex();

    /**
     * 根据id查询日志标题
     *
     * @param id 日志id
     * @return id对应的日志对象
     */
    Blog selectTitleByPrimaryKey(int id);

    /**
     * 查询日志对应的编辑器类型
     *
     * @param blogId 日志id
     * @return 编辑器类型
     */
    boolean selectEditorById(Integer blogId);

    /**
     * 插入md
     *
     * @param blog 日志
     */
    void insertMarkdown(Blog blog);

    /**
     * 更新markdown文本
     *
     * @param blog 日志对象
     */
    void updateMarkdown(Blog blog);

    /**
     * 根据日志id查询markdown文本
     *
     * @param id 日志id
     * @return markdown源码
     */
    String selectMdContentByPrimaryKey(int id);
}
