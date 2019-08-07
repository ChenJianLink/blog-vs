package cn.chenjianlink.blogv2.service;

import cn.chenjianlink.blogv2.pojo.Blogger;

/**
 * 站长service
 *
 * @author chenjian
 */
public interface BloggerService {
    /**
     * 前台Master信息展示
     *
     * @return 站长对象
     */
    Blogger findBlogger();

    /**
     * 后台Master信息回显
     *
     * @return 站长对象
     */
    Blogger findBloggerAll();

    /**
     * 修改Master信息
     *
     * @param blogger 封装要修改的信息的站长对象
     */
    void editBloggerInfo(Blogger blogger);

    /**
     * 修改密码
     *
     * @param blogger 封装新密码和新颜值的站长对象
     * @return 日志系统自定义响应
     */
    void updatePassword(Blogger blogger);

    /**
     * 查找密码
     *
     * @return 站长密码和颜值
     */
    Blogger findPassword();
}
