package cn.chenjianlink.blogv2.service;

import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.utils.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 日志service
 *
 * @author chenjian
 */
public interface BlogService {
    /**
     * 查询日志列表
     *
     * @param title 标题（可为空）
     * @param page  当前页码（不为空）
     * @param rows  每页查询记录数（不为空）
     * @return EasyUI响应类
     */
    EasyUiResult findBlogList(String title, Integer page, Integer rows);

    /**
     * 根据id查询日志
     *
     * @param id 日志id
     * @return id对应的日志对象
     */
    Blog findBlogById(Integer id);

    /**
     * 根据id删除日志
     *
     * @param ids 要删除的日志的id数组
     * @throws BlogSearchException 删除日志索引异常
     */
    void deleteBlog(Integer[] ids) throws BlogSearchException;

    /**
     * 修改日志内容
     *
     * @param blog 要修改的日志对象
     * @throws BlogSearchException 更新日志索引异常
     */
    void editBlog(Blog blog) throws BlogSearchException;

    /**
     * 添加新日志
     *
     * @param blog 要添加的日志对象
     * @throws BlogSearchException 添加日志索引异常
     */
    void addBlog(Blog blog) throws BlogSearchException;

    /**
     * 根据发布日期查询日志
     *
     * @return 对应发布日期区间内所有日志
     */
    List<Blog> findBlogDateList();

    /**
     * 前台日志列表展示
     *
     * @param page    要请求页面页码
     * @param blogMap 封装日志发布日期和日志类别的map(两者均可为空)
     * @return 包含日志对象的页面分页对象
     */
    PageResult findBlogList(Integer page, Map<String, Object> blogMap);

    /**
     * 根据条件查询日志
     *
     * @param page  要请求页面页码
     * @param query 查询关键字
     * @return 封装与关键字相关的日志对象的页面分页对象
     * @throws BlogSearchException 查询日志索引异常
     */
    PageResult searchBlogByQuery(Integer page, String query) throws BlogSearchException;

    /**
     * 更新日志阅读量
     *
     * @param blog 要更新的日志对象
     */
    void updateClick(Blog blog);

    /**
     * 获得上一篇博客
     *
     * @param blog 当前日志对象
     * @return 晚于当前日志发布的日志对象（可能为空）
     */
    Blog findPreBlog(Blog blog);

    /**
     * 获得下一篇博客
     *
     * @param blog 当前日志对象
     * @return 早于当前日志发布的日志对象（可能为空）
     */
    Blog findNextBlog(Blog blog);

    /**
     * 更新全部日志索引
     *
     * @throws BlogSearchException 重建索引异常
     */
    void updateBlogIndex() throws BlogSearchException;

    /**
     * 根据id查询日志使用的编辑器
     *
     * @param blogId 日志id
     * @return 编辑器类型(false为md, true为富文本)
     */
    Boolean selectEditorByBlogId(Integer blogId);

    /**
     * 用于后台查询日志
     *
     * @param id 日志id
     * @return 日志对象
     */
    Blog findBlogByIdForAdmin(Integer id);
}
