package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.link.LinkException;
import cn.chenjianlink.blogv2.pojo.EasyUiResult;
import cn.chenjianlink.blogv2.pojo.Link;
import cn.chenjianlink.blogv2.service.LinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 友情链接管理Controller
 *
 * @author chenjian
 */
@RestController
@RequestMapping("/admin/linkManage")
public class LinkManageController {

    @Resource
    private LinkService linkService;

    /**
     * 友情链接管理页面的友情链接列表展示
     *
     * @param page 请求页面页码
     * @param rows 每页数据数
     * @return EasyUi响应
     */
    @PostMapping(value = "/link/list")
    public EasyUiResult getLinkList(Integer page, Integer rows) {
        EasyUiResult linkList = linkService.getLinkList(page, rows);
        return linkList;
    }

    /**
     * 添加友情链接
     *
     * @param link 友情链接对象
     */
    @PostMapping(value = "/link")
    public void addLink(Link link) {
        linkService.addLink(link);
    }

    /**
     * 修改友情链接
     *
     * @param id   修改友情链接的id
     * @param link 修改内容
     */
    @PutMapping(value = "/link/{id}")
    public void editLink(@PathVariable(value = "id", required = true) Integer id, Link link) throws LinkException {
        link.setId(id);
        linkService.editLink(link);
    }

    /**
     * 删除友情链接
     *
     * @param ids 要删除的友情链接id序列
     */
    @DeleteMapping(value = "/link/{ids}")
    public void deleteLink(@PathVariable(value = "ids", required = true) Integer[] ids) {
        linkService.deleteLink(ids);
    }

}
