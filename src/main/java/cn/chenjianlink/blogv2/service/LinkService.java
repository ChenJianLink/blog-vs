package cn.chenjianlink.blogv2.service;

import cn.chenjianlink.blogv2.exception.link.LinkException;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.pojo.Link;

import java.util.List;

/**
 * 友情链接service
 *
 * @author chenjian
 */
public interface LinkService {
    /**
     * 查询所有链接
     *
     * @return 链接列表(首页使用)
     */
    List<Link> getLinkList();

    /**
     * 分页查询所有链接（后台使用）
     *
     * @param page 要请求页面页码
     * @param rows 每页要查询的记录数
     * @return 封装查询结果的EasyUI响应类
     */
    EasyUiResult getLinkList(Integer page, Integer rows);

    /**
     * 保存新链接
     *
     * @param link 要添加的链接对象
     */
    void addLink(Link link);

    /**
     * 修改链接
     *
     * @param link 要修改的链接对象
     * @throws LinkException 友情链接异常
     */
    void editLink(Link link) throws LinkException;

    /**
     * 删除链接
     *
     * @param ids 要删除的链接的id数组
     */
    void deleteLink(Integer[] ids);
}
