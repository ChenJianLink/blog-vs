package cn.chenjianlink.blogv2.controller;

import cn.chenjianlink.blogv2.exception.other.IpAddressQueryException;
import cn.chenjianlink.blogv2.pojo.Message;
import cn.chenjianlink.blogv2.service.MessageService;
import cn.chenjianlink.blogv2.utils.AddressUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 留言板块Controller
 *
 * @author chenjian
 */
@Controller
public class MessageController {


    @Resource
    private MessageService messageService;

    /**
     * 留言板展示
     *
     * @param model 页面视图模型
     * @return 主页面
     */
    @GetMapping("/message/messagesboard")
    public String showMessageboard(Model model) {
        List<Message> messageList = messageService.findMessageList();
        Message message = messageList.get(0);
        System.out.println(message.getLeaveMessageDate());
        model.addAttribute("messageList", messageList);
        model.addAttribute("mainPage", "foreground/message/messagesboard.html");
        model.addAttribute("pageTitle", "游客留言-局外人之秘境");
        ControllerMethod.showMainTemp(model);
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
        AddressUtils addressUtils = new AddressUtils();
        String addressInfo = addressUtils.getAddresses(userIp);
        message.setUserIp(userIp);
        message.setIpAddressInfo(addressInfo);
        messageService.addMessage(message);
    }
}
