package cn.chenjianlink.blogv2.test;

import cn.chenjianlink.blogv2.exception.other.IpAddressQueryException;
import cn.chenjianlink.blogv2.utils.AddressUtils;
import org.junit.Test;

/**
 * ip地址工具测试类
 *
 * @author chenjian
 */
public class AddressUtilsTest {
    @Test
    public void ipTest() throws IpAddressQueryException {
        AddressUtils addressUtils = new AddressUtils();
        String addresses = addressUtils.getAddresses("117.136.102.163");
        System.out.println(addresses);
    }
}
