package cn.chenjianlink.blogv2.utils;

import cn.chenjianlink.blogv2.exception.other.IpAddressQueryException;
import cn.chenjianlink.blogv2.pojo.ipInfo.IpAddressInfo;
import cn.chenjianlink.blogv2.pojo.ipInfo.Result;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * ip地址接口工具类
 *
 * @author chenjian
 */
public class AddressUtils {

    /**
     * 聚合数据的key
     */
    @Value("${blog.ipKey}")
    private String ipKey;

    /**
     * 调用聚合数据的接口
     */
    private final String urlStr = "http://apis.juhe.cn/ip/ipNew";

    private final int errorCode1 = 200103;
    private final int errorCode2 = 200105;

    /**
     * 获取ip地址的方法
     *
     * @param ipAddress ip地址
     * @return ip对应的地理信息
     * @throws IpAddressQueryException ip查询异常
     */
    public String getAddresses(String ipAddress) throws IpAddressQueryException {
        // 字符串拼接
        StringBuilder url = new StringBuilder(urlStr);
        url.append("?ip=").append(ipAddress).append("&key=").append(ipKey);
        // 从http://apis.juhe.cn/ip/ipNew取得IP所在的省市区信息
        String returnStr = "[" + this.getResult(url.toString(), "UTF-8") + "]";
        // 解析JSON,果然还是阿里巴巴厉害
        List<IpAddressInfo> ipAddressInfos = JSON.parseArray(returnStr, IpAddressInfo.class);
        IpAddressInfo ipAddressInfo = ipAddressInfos.get(0);
        if (ipAddressInfo.getResultCode() != errorCode1 && ipAddressInfo.getResultCode() != errorCode2) {
            return ipAddressInfo.getReason();
        }
        if (ipAddressInfo.getErrorCode() != 0) {
            //若服务级错误（非200103和200105）和系统级错误，则直接抛出异常
            throw new IpAddressQueryException("ip查询异常：" + ipAddressInfo.getReason());
        }
        Result result = ipAddressInfo.getResult();
        String ipInfo = result.getCountry() + " " + result.getProvince() + " " + result.getCity() + " " + result.getIsp();
        return ipInfo;
    }

    /**
     * @param urlStr   请求的地址
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return ip对应的信息
     */
    private String getResult(String urlStr, String encoding) throws IpAddressQueryException {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            // 新建连接实例
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间，单位毫秒
            connection.setConnectTimeout(2000);
            // 设置读取数据超时时间，单位毫秒
            connection.setReadTimeout(2000);
            // 是否打开输出流 true|false
            connection.setDoOutput(true);
            // 是否打开输入流true|false
            connection.setDoInput(true);
            // 提交方法POST|GET
            connection.setRequestMethod("POST");
            // 是否缓存true|false
            connection.setUseCaches(false);
            // 打开连接端口
            connection.connect();
            // 打开输出流往对端服务器写数据
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            // 刷新
            out.flush();
            // 关闭输出流
            out.close();
            // 往对端写完数据对端服务器返回数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            // 以BufferedReader流来读取
            StringBuilder buffer = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            throw new IpAddressQueryException("ip查询异常：" + e.getMessage(), e);
        } finally {
            if (connection != null) {
                // 关闭连接
                connection.disconnect();
            }
        }
    }
}
