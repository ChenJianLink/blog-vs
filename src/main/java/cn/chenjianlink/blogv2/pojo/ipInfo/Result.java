package cn.chenjianlink.blogv2.pojo.ipInfo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ip信息中封装位置信息的部分
 *
 * @author chenjian
 */
@Setter
@Getter
@ToString
public class Result {

    /**
     * ip所在国家
     */
    @JSONField(name = "Country")
    private String country;
    /**
     * 省份
     */
    @JSONField(name = "Province")
    private String province;
    /**
     * 城市
     */
    @JSONField(name = "City")
    private String city;
    /**
     * 运营商
     */
    @JSONField(name = "Isp")
    private String isp;


}