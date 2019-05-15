package cn.chenjianlink.blogv2.service;

import cn.chenjianlink.blogv2.exception.blogtype.BlogTypeException;
import cn.chenjianlink.blogv2.pojo.BlogType;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.utils.BlogResult;

import java.util.List;

/**
 * 日志类别service
 *
 * @author chenjian
 */
public interface BlogTypeService {
    /**
     * 查询所有日志类别
     *
     * @return 日志类别列表
     */
    List<BlogType> getBlogTypeCountList();

    /**
     * 分页查询所有日志类别（后台系统使用）
     *
     * @param page 要请求页面页码
     * @param rows 每页要查询的记录数
     * @return 封装查询结果的EasyUI响应类
     */
    EasyUiResult getBlogTypeList(Integer page, Integer rows);

    /**
     * 添加日志类别
     *
     * @param blogType 要添加的日志类别对象
     */
    void addBlogType(BlogType blogType);

    /**
     * 修改日志类别
     *
     * @param blogType 要修改的日志类别对象
     * @throws BlogTypeException 日志类别异常
     */
    void editBlogType(BlogType blogType) throws BlogTypeException;

    /**
     * 删除日志类别
     *
     * @param ids 要删除的日志类别的id数组
     * @return 成功信息
     */
    BlogResult deleteBlogType(Integer[] ids);
}
