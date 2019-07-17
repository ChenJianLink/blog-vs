package cn.chenjianlink.blogv2.utils.lucene;

import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.pojo.Blog;

import java.util.List;

/**
 * 使用lucene实现前台博客查询
 *
 * @author chenjian
 */
public interface BlogSearch {

    /**
     * 添加日志索引
     *
     * @param blog 要添加索引的日志对象
     * @throws BlogSearchException 日志索引添加异常，附带异常信息
     */
    void addBlogIndex(Blog blog) throws BlogSearchException;

    /**
     * 删除日志索引
     *
     * @param blogIds 要删除的日志索引的日志id数组
     * @throws BlogSearchException 日志索引删除异常，附带异常信息
     */
    void deleteBlogIndex(Integer... blogIds) throws BlogSearchException;

    /**
     * 更新日志索引
     *
     * @param blog 要更新索引的日志对象
     * @throws BlogSearchException 日志索引更新异常，附带异常信息
     */
    void updateBlogIndex(Blog blog) throws BlogSearchException;

    /**
     * 根据关键字查询日志索引
     *
     * @param query 查询的关键字
     * @return 关键字对应的所有日志
     * @throws BlogSearchException 日志索引查询异常，附带异常信息
     */
    List<Blog> searchBlogIndex(String query) throws BlogSearchException;

    /**
     * 重建所有日志索引，更新全部日志索引
     *
     * @param blogList 要更新的日志对象序列
     * @throws BlogSearchException 重建日志索引异常
     */
    void rebuildBlogIndex(List<Blog> blogList) throws BlogSearchException;
}
