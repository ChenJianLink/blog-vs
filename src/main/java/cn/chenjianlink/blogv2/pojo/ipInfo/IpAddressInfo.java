package cn.chenjianlink.blogv2.pojo.ipInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Ip查询信息包装类
 *
 * @author chenjian
 */
@Setter
@Getter
@ToString
public class IpAddressInfo {

    /**
     * 查询结果代码
     */
    @JSONField(name = "resultcode")
    private Integer resultCode;
    /**
     * 查询结果信息
     */
    private String reason;
    /**
     * 查询信息
     */
    private Result result;
    /**
     * 错误代码
     */
    @JSONField(name = "error_code")
    private Integer errorCode;


}