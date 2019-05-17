package cn.chenjianlink.blogv2.controller.admin;

import cn.chenjianlink.blogv2.controller.ControllerMethod;
import cn.chenjianlink.blogv2.exception.blogger.BloggerException;
import cn.chenjianlink.blogv2.pojo.Blogger;
import cn.chenjianlink.blogv2.service.BloggerService;
import cn.chenjianlink.blogv2.utils.BlogResult;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 后台Master信息管理Controller
 *
 * @author chenjian
 */
@RestController
@RequestMapping("/admin/bloggerManage")
public class BloggerManageController {

    @Resource
    private BloggerService bloggerService;

    private static final String POINT = ".";

    private static final String VERIFICATION_FORMAT = "^.*(jpg|png|jpeg|gif|bmp|ico)$";

    @Value("${blog.userImagesPath}")
    private String imagePath;

    /**
     * 修改用户信息页面对用户信息进行回显
     *
     * @return 站长对象
     */
    @GetMapping("/blogger")
    public Blogger findBloggerInfo() {
        Blogger blogger = bloggerService.findBloggerAll();
        return blogger;
    }

    /**
     * 修改站长信息
     *
     * @param blogger   要修改的信息
     * @param imageFile 上传的头像
     * @return 业务处理信息
     * @throws BloggerException 站长异常
     */
    @PutMapping("/blogger")
    public BlogResult editBloggerInfo(Blogger blogger, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws BloggerException {
        //判断是否有图片上传
        if (!imageFile.isEmpty()) {
            //原始文件名称
            String pictureName = imageFile.getOriginalFilename().toLowerCase();
            try {
                //校验文件格式
                if (!pictureName.matches(VERIFICATION_FORMAT)) {
                    return BlogResult.showError("上传的图片格式不符合规范");
                }
                //判断是否为恶意程序
                BufferedImage bufferedImage = ImageIO.read(imageFile.getInputStream());
                if (bufferedImage == null || bufferedImage.getHeight() == 0 || bufferedImage.getWidth() == 0) {
                    return BlogResult.showError("上传文件不是图片");
                }
                //设置新文件名
                String newPictureName = UUID.randomUUID().toString() + pictureName.substring(pictureName.lastIndexOf(POINT));
                //上传图片
                File uploadPic = new File(imagePath + newPictureName);
                if (uploadPic.exists()) {
                    uploadPic.mkdirs();
                }
                //向磁盘写文件
                imageFile.transferTo(uploadPic);
                //将图片名称写入pojo
                blogger.setImageName(newPictureName);
            } catch (IOException e) {
                throw new BloggerException("头像上传异常：" + e.getMessage(), e);
            }
        }
        this.bloggerService.editBloggerInfo(blogger);
        return BlogResult.ok();
    }

    /**
     * 修改密码
     *
     * @param oldPassword 原密码
     * @param password    新密码
     * @return 成功信息
     */
    @PutMapping("/blogger/modifyPassword")
    public BlogResult modifyPassword(@RequestParam(value = "oldPassword", required = true) String oldPassword, @RequestParam(value = "newPassword", required = true) String password) {
        Blogger oldBlogger = bloggerService.findPassword();
        String certificate = oldBlogger.getPassword();
        String encryptPassword = ControllerMethod.encrypt(oldPassword, oldBlogger.getSalt());
        //对原密码进行对比判断
        if (!certificate.equals(encryptPassword)) {
            return BlogResult.showError("密码修改失败,原密码不正确");
        }
        //加密新密码,设置新颜值
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        String newPassword = ControllerMethod.encrypt(password, salt);
        oldBlogger.setPassword(newPassword);
        oldBlogger.setSalt(salt);
        this.bloggerService.updatePassword(oldBlogger);
        return BlogResult.ok();
    }
}