package cn.chenjianlink.blogv2.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 腾讯云对象存储
 *
 * @author chenjian
 */
@Service
public class TencentCloudCosUtils {

    @Value("${tencentCloud.secretId}")
    private String secretId;

    @Value("${tencentCloud.secretKey}")
    private String secretKey;

    @Value("${tencentCloud.region}")
    private String region;

    /**
     * 获取cos客户端
     *
     * @return cos客户端
     */
    public COSClient getCosClent() {
        // 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 配置使用 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 生成 cos 客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }
}
