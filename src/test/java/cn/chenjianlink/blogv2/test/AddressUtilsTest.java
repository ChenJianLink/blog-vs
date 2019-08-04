package cn.chenjianlink.blogv2.test;

import cn.chenjianlink.blogv2.BlogApplication;
import cn.chenjianlink.blogv2.exception.other.IpAddressQueryException;
import cn.chenjianlink.blogv2.utils.AddressUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * ip地址工具测试类
 *
 * @author chenjian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BlogApplication.class)
public class AddressUtilsTest {

    @Resource
    private AddressUtils addressUtils;

    @Test
    public void ipTest() throws IpAddressQueryException {
        String addresses = addressUtils.getAddresses("117.136.102.163");
        System.out.println(addresses);
    }
}
