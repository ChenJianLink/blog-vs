package cn.chenjianlink.blogv2.service.impl;

import cn.chenjianlink.blogv2.exception.blogtype.BlogTypeException;
import cn.chenjianlink.blogv2.mapper.BlogTypeMapper;
import cn.chenjianlink.blogv2.pojo.BlogType;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.service.BlogTypeService;
import cn.chenjianlink.blogv2.utils.BlogResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志类别管理service
 *
 * @author chenjian
 */
@Service
public class BlogTypeServiceImpl implements BlogTypeService {

    @Resource
    private BlogTypeMapper blogTypeMapper;

    /**
     * 查询所有类别以及日志数量
     */
    @Override
    @Cacheable(value = "blogTypeCache")
    public List<BlogType> getBlogTypeCountList() {
        List<BlogType> blogTypes = blogTypeMapper.selectAll();
        return blogTypes;
    }

    /**
     * 分页查询所有类别
     */
    @Override
    @Cacheable(value = "blogTypeCache")
    public EasyUiResult getBlogTypeList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<BlogType> typeList = blogTypeMapper.selectList();
        PageInfo<BlogType> pageInfo = new PageInfo<>(typeList);
        long total = pageInfo.getTotal();
        EasyUiResult result = new EasyUiResult(total, typeList);
        return result;
    }

    /**
     * 添加日志类别
     */
    @Override
    @CacheEvict(value = "blogTypeCache", allEntries = true)
    public void addBlogType(BlogType blogType) {
        blogTypeMapper.insert(blogType);
    }

    /**
     * 修改日志类别
     */
    @Override
    @CacheEvict(value = "blogTypeCache", allEntries = true)
    public void editBlogType(BlogType blogType) throws BlogTypeException {
        BlogType oldType = blogTypeMapper.selectByPrimaryKey(blogType.getId());
        if (oldType == null) {
            throw new BlogTypeException("没有id为" + blogType.getId() + "的日志类别");
        }
        if (blogType != null) {
            //非法输入判断
            if (!blogType.getTypeName().isEmpty() && blogType.getTypeName().trim().length() > 0) {
                oldType.setTypeName(blogType.getTypeName());
            }
            if (blogType.getOrderNo() != null) {
                oldType.setOrderNo(blogType.getOrderNo());
            }
            blogTypeMapper.update(oldType);
        }
    }

    /**
     * 删除日志类别
     */
    @Override
    @CacheEvict(value = "blogTypeCache", allEntries = true)
    public BlogResult deleteBlogType(Integer[] ids) {
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        List<BlogType> blogTypes = blogTypeMapper.selectTypeCount(id);
        //判断要删除的日志类别下是否有日志
        for (BlogType blogType : blogTypes) {
            if (blogType.getBlogCount() > 0) {
                return BlogResult.showError("编号为" + blogType.getId() + "的类别下存在日志,请先删除相关日志，再删除该分类");
            }
        }
        blogTypeMapper.delete(id);
        return BlogResult.ok();
    }
}
