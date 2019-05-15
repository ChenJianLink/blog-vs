package cn.chenjianlink.blogv2.mapper;

import cn.chenjianlink.blogv2.pojo.Blogger;
import org.springframework.stereotype.Repository;

/**
 * 站长管理Mapper
 *
 * @author chenjian
 */
@Repository
public interface BloggerMapper {
    /**
     * 前台Master信息查询
     *
     * @return 当前站长对象(部分信息)
     */
    Blogger selectSome();

    /**
     * 后台Master信息查询
     *
     * @return 当前站长对象(除密码和颜值以外的所有信息)
     */
    Blogger selectAll();

    /**
     * Master信息修改
     *
     * @param blogger 当前站长对象
     */
    void update(Blogger blogger);

    /**
     * 查找密码和颜值
     *
     * @return 当前站长密码与颜值
     */
    Blogger selectPassword();
}
