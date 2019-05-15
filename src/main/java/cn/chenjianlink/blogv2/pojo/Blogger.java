package cn.chenjianlink.blogv2.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Master信息对象
 *
 * @author chenjian
 */
@Getter
@Setter
public class Blogger implements Serializable {
    private Integer id;
    private String userName;
    private String password;
    /**
     * 颜值
     */
    private String salt;
    /**
     * Master个人简介
     */
    private String profile;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 个性签名
     */
    private String sign;
    /**
     * 头像
     */
    private String imageName;

}
