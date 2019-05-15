package cn.chenjianlink.blogv2.test;

import cn.chenjianlink.blogv2.BlogApplication;
import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.mapper.BlogMapper;
import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.BlogType;
import cn.chenjianlink.blogv2.utils.PageResult;
import cn.chenjianlink.blogv2.service.BlogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 日志服务层测试类
 *
 * @author chenjian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BlogApplication.class)
public class BlogServiceTest {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private BlogService blogService;

    /**
     * 测试blogMapper
     */
    @Test
    public void blogMapperTset() {
        Blog blog = new Blog();
        blog.setTitle("测试");
        blog.setContent("heheheh");
        BlogType blogType = new BlogType();
        blogType.setId(8);
        blog.setBlogType(blogType);
        blog.setState(1);
        blog.setSummary("hahahha");
        blog.setReleaseDate(new Date());

        blogMapper.insert(blog);
        System.out.println(blog);
    }

    @Test
    public void blogServiceTset() throws BlogSearchException {
        Blog blog = new Blog();
        blog.setTitle("测试");
        blog.setContent("heheheh");
        BlogType blogType = new BlogType();
        blogType.setId(8);
        blog.setBlogType(blogType);
        blog.setState(1);
        blog.setSummary("hahahha");
        blog.setReleaseDate(new Date());

        blogService.addBlog(blog);
        System.out.println(blog);
    }

    /**
     * 测试重建索引方法
     *
     * @throws BlogSearchException 重建异常
     */
    @Test
    public void updateBlogIndexTest() throws BlogSearchException {
        blogService.updateBlogIndex();
        PageResult result = blogService.searchBlogByQuery(1, "缀物");
        //查询测试是否重建成功
        List pageList = result.getPageList();
        Iterator iterator = pageList.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            System.out.println(next.toString());
        }
    }
}
