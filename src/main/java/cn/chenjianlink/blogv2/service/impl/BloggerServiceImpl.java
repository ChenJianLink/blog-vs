package cn.chenjianlink.blogv2.service.impl;


import cn.chenjianlink.blogv2.mapper.BloggerMapper;
import cn.chenjianlink.blogv2.pojo.Blogger;
import cn.chenjianlink.blogv2.service.BloggerService;
import cn.chenjianlink.blogv2.utils.BlogResult;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Master信息处理服务层
 *
 * @author chenjian
 */
@Service
public class BloggerServiceImpl implements BloggerService {

    @Resource
    private BloggerMapper bloggerMapper;

    /**
     * 前台Master信息展示
     */
    @Override
    @Cacheable(value = "bloggerCache")
    public Blogger findBlogger() {
        Blogger blogger = bloggerMapper.selectSome();
        return blogger;
    }

    /**
     * 后台Master信息回显
     */
    @Override
    public Blogger findBloggerAll() {
        Blogger blogger = bloggerMapper.selectAll();
        return blogger;
    }

    /**
     * 编辑站长信息
     */
    @Override
    @CacheEvict(value = "bloggerCache", allEntries = true)
    public void editBloggerInfo(Blogger blogger) {
        bloggerMapper.update(blogger);
    }

    /**
     * 修改密码
     */
    @Override
    @CacheEvict(value = "bloggerCache", allEntries = true)
    public void updatePassword(Blogger blogger) {
        bloggerMapper.update(blogger);
    }

    /**
     * 查找用户名密码
     */
    @Override
    public Blogger findPassword() {
        Blogger blogger = bloggerMapper.selectPassword();
        return blogger;
    }
}
