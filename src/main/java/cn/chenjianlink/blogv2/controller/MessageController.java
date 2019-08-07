package cn.chenjianlink.blogv2.controller;

import cn.chenjianlink.blogv2.exception.other.IpAddressQueryException;
import cn.chenjianlink.blogv2.pojo.Message;
import cn.chenjianlink.blogv2.service.MessageService;
import cn.chenjianlink.blogv2.utils.AddressUtils;
import cn.chenjianlink.blogv2.utils.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 留言板块Controller
 *
 * @author chenjian
 */
@Controller
public class MessageController {


    @Resource
    private MessageService messageService;

    @Resource
    private AddressUtils addressUtils;
    @Resource
    private ControllerMethod controllerMethod;

    /**
     * 留言板展示
     *
     * @param model   页面视图模型
     * @param page    留言的页面号
     * @param request htttp请求
     * @return 主页面
     */
    @GetMapping("/message/messagesboard")
    public String showMessageboard(Model model, HttpServletRequest request, @RequestParam(value = "page", required = false) Integer page) {
        //判断page是否为空,为空则设置为1
        if (page == null || page <= 0) {
            page = 1;
        }
        PageResult pageResult = messageService.findMessageList(page);
        String url = controllerMethod.getUrl(request);
        pageResult.setUrl(url);
        model.addAttribute("page", pageResult);
        model.addAttribute("messageList", pageResult.getPageList());
        model.addAttribute("mainPage", "foreground/message/messagesboard.html");
        model.addAttribute("pageTitle", "游客留言-局外人之秘境");
        controllerMethod.showMainTemp(model);
        return "mainTemp";
    }

    /**
     * 留言
     *
     * @param message 留言对象
     * @param request 请求
     */
    @PostMapping("/message/leave")
    @ResponseBody
    public void leaveMessage(Message message, HttpServletRequest request) throws IpAddressQueryException {
        String userIp = request.getRemoteAddr();
        String addressInfo = addressUtils.getAddresses(userIp);
        message.setUserIp(userIp);
        message.setIpAddressInfo(addressInfo);
        messageService.addMessage(message);
    }
}
