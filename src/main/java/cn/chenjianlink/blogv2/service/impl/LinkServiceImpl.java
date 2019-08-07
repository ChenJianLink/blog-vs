package cn.chenjianlink.blogv2.service.impl;


import cn.chenjianlink.blogv2.exception.link.LinkException;
import cn.chenjianlink.blogv2.mapper.LinkMapper;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.pojo.Link;
import cn.chenjianlink.blogv2.service.LinkService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 友情链接服务层
 *
 * @author chenjian
 */
@Service
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkMapper linkMapper;

    /**
     * 查询所有链接
     */
    @Override
    @Cacheable(value = "linkCache")
    public List<Link> getLinkList() {
        List<Link> linkList = linkMapper.selectList();
        return linkList;
    }

    /**
     * 分页查询友情链接
     */
    @Override
    @Cacheable(value = "linkCache")
    public EasyUiResult getLinkList(Integer page, Integer rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //查询
        List<Link> linkList = linkMapper.selectAll();
        //获得结果
        PageInfo<Link> pageInfo = new PageInfo<>(linkList);
        long total = pageInfo.getTotal();
        EasyUiResult result = new EasyUiResult(total, linkList);
        return result;
    }

    /**
     * 保存新链接
     */
    @Override
    @CacheEvict(value = "linkCache", allEntries = true)
    public void addLink(Link link) {
        linkMapper.insert(link);
    }

    /**
     * 修改链接
     */
    @Override
    @CacheEvict(value = "linkCache", allEntries = true)
    public void editLink(Link link) throws LinkException {
        Link oldlink = linkMapper.selectByPrimaryKey(link.getId());
        if (oldlink == null) {
            throw new LinkException("没有id为" + link.getId() + "的友情链接");
        }
        //数据更新
        //非法输入判断
        if (link != null) {
            if (!link.getLinkName().isEmpty() && link.getLinkName().trim().length() > 0) {
                oldlink.setLinkName(link.getLinkName());
            }
            if (!link.getLinkUrl().isEmpty() && link.getLinkUrl().trim().length() > 0) {
                oldlink.setLinkUrl(link.getLinkUrl());
            }
            if (link.getOrderNo() != null) {
                oldlink.setOrderNo(link.getOrderNo());
            }
            linkMapper.update(oldlink);
        }
    }

    /**
     * 删除链接
     */
    @Override
    @CacheEvict(value = "linkCache", allEntries = true)
    public void deleteLink(Integer[] ids) {
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        linkMapper.delete(id);
    }
}
