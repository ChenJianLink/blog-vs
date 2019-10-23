package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.blog.BlogSearchException;
import cn.chenjianlink.blogv2.pojo.Blog;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.service.BlogService;
import cn.chenjianlink.blogv2.utils.TencentCloudCosUtils;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;


/**
 * 日志管理Controller
 *
 * @author chenjian
 */
@Slf4j
@RestController
@RequestMapping("/admin/blogManage")
public class BlogManageController {

    @Resource
    private BlogService blogService;

    @Value("${tencentCloud.bucketName}")
    private String bucketName;

    @Value("${tencentCloud.prefix}")
    private String prefix;

    @Value("${tencentCloud.path}")
    private String path;

    @Resource
    private TencentCloudCosUtils tencentCloudCosUtils;

    @Value("${tencentCloud.tmpPath}")
    private String tmpPath;

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
     * 上传图片
     *
     * @return
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

        // 处理上传图片的名字以及上传路径
        String oldImageName = imageFile.getOriginalFilename();
        String imageSuffix = oldImageName.substring(oldImageName.lastIndexOf("."));
        String newImageName = UUID.randomUUID() + imageSuffix;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        // 指定要上传到 COS 上对象键
        String key = "/" + prefix + "/" + year + "/" + month + "/" + day + "/" + newImageName;

        // 获取cos客户端
        COSClient cosClient = tencentCloudCosUtils.getCosClent();

        try {
            // 指定要上传的文件
            File uploadFile = new File(tmpPath + newImageName);
            FileUtils.copyInputStreamToFile(imageFile.getInputStream(), uploadFile);
            // 指定要上传到的存储桶
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, uploadFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            // 上传完成后删除临时文件
            uploadFile.delete();
            //返回成功信息
            jsonObject.put("success", 1);
            jsonObject.put("message", "图片上传成功");
            jsonObject.put("url", path + putObjectRequest.getKey());
        } catch (IOException e) {
            log.error("图片上传IO异常:" + e.getMessage(), e);
            jsonObject.put("success", 0);
            jsonObject.put("message", "图片上传失败，文件转储异常");
            jsonObject.put("url", null);
            return jsonObject;
        } catch (CosClientException clientException) {
            log.error("图片上传IO异常:" + clientException.getMessage(), clientException);
            jsonObject.put("success", 0);
            jsonObject.put("message", "图片上传失败，客户端或服务端错误");
            jsonObject.put("url", null);
            return jsonObject;
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return jsonObject;
    }
}