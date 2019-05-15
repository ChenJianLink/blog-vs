package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.exception.other.UeditorInitializeException;
import com.baidu.ueditor.ActionEnter;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 实现ueditor中static/ueditor/jsp/controller.jsp页面中的内容
 * 以免SpringBoot不支持jsp导致ueditor不能正常初始化
 *
 * @author chenjian
 */
@Controller
@RequestMapping("/ueditor/jsp")
public class UeditorController {

    /**
     * 初始化ueditor
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/controller.html")
    @ResponseBody
    public void getConfigInfo(HttpServletRequest request, HttpServletResponse response) throws UeditorInitializeException {
        response.setHeader("Content-Type", "text/html");
        String rootPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static";
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            throw new UeditorInitializeException("ueditor初始化失败:" + e.getMessage(), e);
        }
    }
}
