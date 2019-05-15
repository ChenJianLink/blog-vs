package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.blogtype.BlogTypeException;
import cn.chenjianlink.blogv2.pojo.BlogType;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.service.BlogTypeService;
import cn.chenjianlink.blogv2.utils.BlogResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 日志类别管理Controller
 *
 * @author chenjian
 */
@RestController
@RequestMapping("/admin/blogTypeManage")
public class BlogTypeManageController {

    @Resource
    private BlogTypeService blogTypeService;

    /**
     * 展示日志类别列表
     *
     * @param page 要请求页面的页码
     * @param rows 每页数据数
     * @return 日志类别列表
     */
    @PostMapping(value = "/blogType/list")
    public EasyUiResult getBlogTypeList(Integer page, Integer rows) {
        EasyUiResult result = blogTypeService.getBlogTypeList(page, rows);
        return result;
    }

    /**
     * 添加日志类别
     *
     * @param blogType 日志类别对象
     */
    @PostMapping(value = "/blogType")
    public void addBlogType(BlogType blogType) {
        blogTypeService.addBlogType(blogType);
    }

    /**
     * 修改日志类别
     *
     * @param blogType 日志类别对象
     * @throws BlogTypeException 日志类别异常
     */
    @PutMapping(value = "/blogType/{id}")
    public void editBlodType(@PathVariable(value = "id", required = true) Integer id, BlogType blogType) throws BlogTypeException {
        blogType.setId(id);
        blogTypeService.editBlogType(blogType);
    }

    /**
     * 删除日志类别
     *
     * @param ids 要删除的日志id
     * @return 删除结果
     */
    @DeleteMapping(value = "/blogType/{ids}")
    public BlogResult deleteBlogType(@PathVariable(value = "ids", required = true) Integer[] ids) {
        BlogResult result = blogTypeService.deleteBlogType(ids);
        return result;
    }
}
