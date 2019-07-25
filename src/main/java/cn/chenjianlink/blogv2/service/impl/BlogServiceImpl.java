package cn.chenjianlink.blogv2.service.impl;

import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.utils.lucene.BlogSearch;
import cn.chenjianlink.blogv2.mapper.BlogMapper;
import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.utils.PageResult;
import cn.chenjianlink.blogv2.service.BlogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志相关service
 *
 * @author chenjian
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper blogMapper;
    @Value("${blog.rows}")
    private Integer rows;

    private static final int PUBLISH = 2;

    @Resource
    private BlogSearch blogSearch;

    /**
     * 后台日志管理列表展示(分页查询)
     */
    @Override
    @Cacheable(value = "blogCache")
    public EasyUiResult findBlogList(String title, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Blog> blogList = this.blogMapper.selectList(title);
        PageInfo<Blog> pageInfo = new PageInfo<>(blogList);
        long total = pageInfo.getTotal();
        EasyUiResult result = new EasyUiResult(total, blogList);
        return result;
    }

    /**
     * 根据id查询日志
     */
    @Override
    @Cacheable(value = "blogCache")
    public Blog findBlogById(Integer id) {
        Blog blog = this.blogMapper.selectByPrimaryKey(id);
        return blog;
    }

    /**
     * 删除日志
     */
    @Override
    @CacheEvict(value = {"blogCache", "blogTypeCache", "commentCache"}, allEntries = true)
    public void deleteBlog(Integer[] ids) throws BlogSearchException {
        int[] id = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            id[i] = ids[i];
        }
        this.blogMapper.delete(id);
        //删除索引
        blogSearch.deleteBlogIndex(ids);
    }

    /**
     * 更新日志
     */
    @Override
    @CacheEvict(value = {"blogCache", "blogTypeCache"}, allEntries = true)
    public void editBlog(Blog blog, Boolean isFirstPublish) throws BlogSearchException {
        if (isFirstPublish || blog.getState() != PUBLISH) {
            blog.setReleaseDate(new Date());
        } else {
            Date releaseDate = this.blogMapper.selectReleaseDate(blog.getId());
            blog.setReleaseDate(releaseDate);
        }
        blog.setUpdateDate(new Date());
        //若关键字为空串，则设置为空
        if (blog.getKeyWord() == null || blog.getKeyWord().isEmpty()) {
            blog.setKeyWord(null);
        }
        this.blogMapper.update(blog);
        if (!blog.getIsUeditor()) {
            this.blogMapper.updateMarkdown(blog);
        }
        if (blog.getState() == PUBLISH) {
            //更新索引
            blogSearch.updateBlogIndex(blog);
        } else {
            blogSearch.deleteBlogIndex(blog.getId());
        }
    }

    /**
     * 添加新日志
     */
    @Override
    @CacheEvict(value = {"blogCache", "blogTypeCache"}, allEntries = true)
    public void addBlog(Blog blog) throws BlogSearchException {
        //补全属性
        blog.setReleaseDate(new Date());
        blog.setUpdateDate(new Date());
        this.blogMapper.insert(blog);
        if (!blog.getIsUeditor()) {
            this.blogMapper.insertMarkdown(blog);
        }
        if (blog.getState() == PUBLISH) {
            //添加索引
            blogSearch.addBlogIndex(blog);
        }
    }

    /**
     * 根据日期分类查询日志数量
     */
    @Override
    @Cacheable(value = "blogCache")
    public List<Blog> findBlogDateList() {
        List<Blog> blogList = this.blogMapper.selectCountList();
        return blogList;
    }

    /**
     * 首页日志列表显示
     */
    @Override
    @Cacheable(value = "blogCache")
    public PageResult findBlogList(Integer page, Map<String, Object> blogMap) {
        //对过大的page处理
        int totalRows = this.blogMapper.selectCount(blogMap);
        int totalPage = totalRows / rows;
        totalPage = totalRows % rows == 0 ? totalPage : totalPage + 1;
        page = page <= totalPage ? page : totalPage;

        //设置分页信息
        PageHelper.startPage(page, rows);
        //查询日志
        List<Blog> blogList = this.blogMapper.selectListAll(blogMap);
        //抓取日志中插入的图片，在日志列表中显示(利用jsoup抓取)
        for (Blog blog : blogList) {
            List<String> imagesList = blog.getImagesList();
            String blogContent = blog.getContent();
            Document doc = Jsoup.parse(blogContent);
            Elements images = doc.select("img");
            for (int i = 0; i < images.size(); i++) {
                //将图片url取出并放入到imageList中
                imagesList.add(images.get(i).attr("src"));
                //显示三张图片
                if (i == 2) {
                    break;
                }
            }
        }
        //封装结果
        PageResult result = new PageResult(page, totalRows, rows, blogList);
        return result;
    }

    /**
     * 根据条件查询博客
     */
    @Override
    public PageResult searchBlogByQuery(Integer page, String query) throws BlogSearchException {
        List<Blog> blogList = blogSearch.searchBlogIndex(query);
        int totalRows = blogList.size();
        if (totalRows < 1) {
            //若查询没有结果,则直接返回原来的结果
            return new PageResult(page, totalRows, rows, blogList);
        }
        //对输入过大的page进行处理
        int totalPage = totalRows / rows;
        totalPage = totalRows % rows == 0 ? totalPage : totalPage + 1;
        page = page <= totalPage ? page : totalPage;
        //对结果进行分页处理
        List<Blog> list = blogList.subList(rows * (page - 1), ((rows * page) > totalRows ? totalRows : (rows * page)));
        PageResult result = new PageResult(page, totalRows, rows, list);
        return result;
    }

    /**
     * 更新日志阅读量
     */
    @Override
    @CacheEvict(value = "blogCache", allEntries = true)
    public void updateClick(Blog blog) {
        this.blogMapper.update(blog);
    }

    /**
     * 获得上一篇博客
     */
    @Override
    public Blog findPreBlog(Blog blog) {
        Blog pre = this.blogMapper.selectPre(blog.getReleaseDate());
        return pre;
    }

    /**
     * 获得下一篇博客
     */
    @Override
    public Blog findNextBlog(Blog blog) {
        Blog next = this.blogMapper.selectNext(blog.getReleaseDate());
        return next;
    }

    /**
     * 更新全部日志索引
     */
    @Override
    public void updateBlogIndex() throws BlogSearchException {
        List<Blog> blogList = this.blogMapper.selectBlogForIndex();
        for (Blog blog : blogList) {
            String searchContext = Jsoup.parse(blog.getContent()).text();
            blog.setSearchContent(searchContext);
        }
        this.blogSearch.rebuildBlogIndex(blogList);
    }

    /**
     * 根据id查询日志使用的编辑器
     */
    @Override
    public Blog selectEditorByBlogId(Integer blogId) {
        Blog blog = this.blogMapper.selectEditorById(blogId);
        return blog;
    }

    /**
     * 用于后台查询日志
     */
    @Override
    public Blog findBlogByIdForAdmin(Integer id) {
        Blog blog = blogMapper.selectForAdminByPrimaryKey(id);
        if (!blog.getIsUeditor()) {
            blog.setContent(null);
            String markdown = this.blogMapper.selectMdContentByPrimaryKey(id);
            blog.setMdContent(markdown);
        }
        return blog;
    }


}
