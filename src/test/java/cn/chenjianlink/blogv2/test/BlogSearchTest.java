package cn.chenjianlink.blogv2.test;

import cn.chenjianlink.blogv2.BlogApplication;
import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.lucene.BlogSearch;
import cn.chenjianlink.blogv2.pojo.Blog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 索引测试类
 *
 * @author chenjian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BlogApplication.class)
public class BlogSearchTest {

    @Resource
    private BlogSearch blogSearch;

    @Test
    public void addBlogIndex() throws BlogSearchException {
        Blog blog = new Blog();
        blog.setId(22333);
        blog.setTitle("测试用例blog");
        blog.setReleaseDate(new Date());
        blog.setSearchContent("@Test\n" +
                "    public void deleteBlogIndex() {\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void updateBlogIndex() {\n" +
                "    }");
        blogSearch.addBlogIndex(blog);

        Blog blog1 = new Blog();
        blog1.setId(222222);
        blog1.setSearchContent("@Test\n" +
                "    public void deleteBlogIndex() {\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void updateBlogIndex() {\n" +
                "    }");
        blog1.setTitle("还是测试用例");
        blog1.setReleaseDate(new Date());
        blogSearch.addBlogIndex(blog1);

        Blog blog2 = new Blog();
        blog2.setId(2223322);
        blog2.setSearchContent("@Test\n" +
                "    public void deleteBlogIndex() {\n" +
                "    }\n" +
                "\n" +
                "    @Test\n" +
                "    public void updateBlogIndex() {\n" +
                "    }");
        blog2.setTitle("依旧是测试用例");
        blog2.setReleaseDate(new Date());
        blogSearch.addBlogIndex(blog2);
    }

    @Test
    public void deleteBlogIndex() throws BlogSearchException {
        Integer[] ids = new Integer[3];
        ids[0] = 2223322;
        ids[1] = 222222;
        ids[2] = 22333;
        blogSearch.deleteBlogIndex(ids);
    }

    @Test
    public void updateBlogIndex() {
    }

    @Test
    public void searchBlogIndex() throws BlogSearchException {
        List<Blog> blogList = blogSearch.searchBlogIndex("测试用例");
        System.out.println("一共有" + blogList.size() + "条记录");
        if (blogList != null && blogList.size() >= 0) {
            for (Blog blog : blogList) {
                System.out.println(blog.getTitle());
            }
        }
    }
}