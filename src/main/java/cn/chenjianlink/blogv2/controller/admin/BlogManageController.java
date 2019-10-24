package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.service.BlogService;
import cn.chenjianlink.blogv2.utils.TencentCloudCosUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * 日志管理Controller
 *
 * @author chenjian
 */
@RestController
@RequestMapping("/admin/blogManage")
public class BlogManageController {

    @Resource
    private BlogService blogService;

    @Resource
    private TencentCloudCosUtils tencentCloudCosUtils;


    /**
     * 展示日志列表
     *
     * @param title 日志标题
     * @param page  要请求页面页码
     * @param rows  页面请求数据数
     * @return 日志列表
     */
    @PostMapping(value = "/blogList")
    @ResponseBody
    public EasyUiResult getBlogList(@RequestParam(value = "title", required = false) String title, Integer page, Integer rows) {
        EasyUiResult blogList = blogService.findBlogList(title, page, rows);
        return blogList;
    }

    /**
     * 修改日志页面的数据回显
     *
     * @param id 要修改的日志id
     * @return id对应的日志对象
     */
    @GetMapping(value = "/blog/{blogId}")
    @ResponseBody
    public Blog findBlogInfo(@PathVariable(value = "blogId", required = true) Integer id) {
        Blog blog = blogService.findBlogByIdForAdmin(id);
        return blog;
    }

    /**
     * 删除日志
     *
     * @param ids 要删除的日志id
     * @throws BlogSearchException 日志删除异常
     */
    @DeleteMapping(value = "/blog/{ids}")
    public void deleteBlog(@PathVariable(value = "ids", required = true) Integer[] ids) throws BlogSearchException {
        blogService.deleteBlog(ids);
    }

    /**
     * 更新日志内容
     *
     * @param blog 要修改的日志对象
     * @throws BlogSearchException 日志索引修改异常
     */
    @PutMapping(value = "/blog/{id}")
    public void editBlogInfo(@PathVariable(value = "id", required = true) Integer id, Blog blog, Boolean isFirstPublish) throws BlogSearchException {
        blog.setId(id);
        blogService.editBlog(blog, isFirstPublish);
    }

    /**
     * 增加新日志
     *
     * @param blog 要添加的日志对象
     * @throws BlogSearchException 日志索引添加异常
     */
    @PostMapping(value = "/blog")
    public void addBlog(Blog blog) throws BlogSearchException {
        blogService.addBlog(blog);
    }

    /**
     * 更新全部日志索引
     *
     * @throws BlogSearchException 日志索引更新异常
     */
    @PutMapping(value = "/blog/index")
    public void updateBlogIndex() throws BlogSearchException {
        blogService.updateBlogIndex();
    }

    /**
     * editor.md上传图片
     *
     * @return 上传结果
     */
    @PostMapping(value = "/uploadImage")
    public JSONObject uploadImage(@RequestParam(value = "editormd-image-file") MultipartFile imageFile) {
        JSONObject jsonObject = new JSONObject();
        long maxSize = 1024 * 1024 * 20;
        // 判断图片是否为空
        if (imageFile == null) {
            jsonObject.put("success", 0);
            jsonObject.put("message", "图片为空");
            jsonObject.put("url", null);
            return jsonObject;
        } else if (imageFile.getSize() > maxSize) {
            // 若上传文件超过20M，则拒绝上传
            jsonObject.put("success", 0);
            jsonObject.put("message", "上传的图片超过20M");
            jsonObject.put("url", null);
            return jsonObject;
        }
        String url = tencentCloudCosUtils.uploadFile(imageFile);
        if (url == null) {
            jsonObject.put("success", 0);
            jsonObject.put("message", "图片上传失败");
        } else {
            jsonObject.put("success", 1);
            jsonObject.put("message", "图片上传成功");
        }
        jsonObject.put("url", url);
        return jsonObject;
    }
}