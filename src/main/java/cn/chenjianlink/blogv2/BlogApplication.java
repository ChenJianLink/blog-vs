package cn.chenjianlink.blogv2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 日志系统启动类
 *
 * @author chenjian
 */
@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@MapperScan("cn.chenjianlink.blogv2.mapper")
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

}
