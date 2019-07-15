package cn.chenjianlink.blogv2.config;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置类
 *
 * @author chenjian
 */
@Configuration
public class EhcacheConfig {
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        //设置为享元模式
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }
}
