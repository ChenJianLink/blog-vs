package cn.chenjianlink.blogv2.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

/**
 * 腾讯云对象存储
 *
 * @author chenjian
 */
@Slf4j
@Service
public class TencentCloudCosUtils {

    @Value("${tencentCloud.secretId}")
    private String secretId;

    @Value("${tencentCloud.secretKey}")
    private String secretKey;

    @Value("${tencentCloud.region}")
    private String region;

    @Value("${tencentCloud.bucketName}")
    private String bucketName;

    @Value("${tencentCloud.prefix}")
    private String prefix;

    @Value("${tencentCloud.path}")
    private String path;

    @Value("${tencentCloud.tmpPath}")
    private String tmpPath;

    /**
     * 获取COSClient实例
     *
     * @return COSClient
     */
    private COSClient getCosClient() {
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

    /**
     * editor.md上传图片接口,Ueditor上传文件接口,处理文件名称
     *
     * @param file 要上传的文件
     * @return 上传后的路径
     */
    public String uploadFile(MultipartFile file) {
        // 处理上传文件的名字以及上传路径
        String oldFileName = file.getOriginalFilename();
        String FileSuffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + FileSuffix;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        // 指定要上传到 COS 上对象键
        String key = "/" + prefix + "/" + year + "/" + month + "/" + day + "/" + newFileName;

        return uploadAttachment(newFileName, key, file);
    }

    /**
     * 真正执行上传的方法
     *
     * @param file        要上传的文件
     * @param key         指定要上传到 COS 上对象键
     * @param newFileName 指定新的文件名
     * @return 上传后访问路径
     */
    private String uploadAttachment(String newFileName, String key, MultipartFile file) {
        COSClient cosClient = getCosClient();
        try {
            // 指定要上传的文件
            File uploadFile = new File(tmpPath + newFileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);

            // 指定要上传到的存储桶
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, uploadFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            // 上传完成后删除临时文件
            uploadFile.delete();

            //返回上传路径
            return path + putObjectRequest.getKey();
        } catch (CosClientException clientException) {
            log.error("文件上传异常:" + clientException.getMessage(), clientException);
            return null;
        } catch (IOException e) {
            log.error("文件转储IO异常:" + e.getMessage(), e);
            return null;
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
    }

}
