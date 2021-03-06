package cn.chenjianlink.blogv2.config;

import cn.chenjianlink.blogv2.shiro.BlogRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author chenjian
 */
@Configuration
public class ShiroConfig {

    @Value("${blog.algorithmName}")
    private String algorithmName;

    @Value("${blog.iterations}")
    private Integer iterations;

    @Value("${shiro.loginUrl}")
    private String loginUrl;

    @Value("${shiro.unauthorizedUrl}")
    private String unauthorizedUrl;

    @Value("${spring.cache.ehcache.config}")
    public String ehcacheConfig;


    /**
     * 自定义Realm
     *
     * @return BlogRealm
     */
    @Bean
    public BlogRealm blogRealm() {
        BlogRealm blogRealm = new BlogRealm();
        //配置凭证匹配器
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(algorithmName);
        hashedCredentialsMatcher.setHashIterations(iterations);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        blogRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        blogRealm.setCachingEnabled(true);
        blogRealm.setCacheManager(ehCacheManager());
        return blogRealm;
    }

    /**
     * shiro拦截器
     *
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置SecuritManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        filterMap.put("ssl", new SslFilter());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/admin/blogger/login", "anon,ssl");
        filterChainDefinitionMap.put("/admin/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        //不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,但是此处必须存在,因为shiro-spring-boot-web-starter或查找此Bean,没有会报错
        return new DefaultShiroFilterChainDefinition();
    }

    /**
     * 安全管理器
     *
     * @param blogRealm 自定义Realm
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(@Qualifier("blogRealm") BlogRealm blogRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(blogRealm);
        return securityManager;
    }

    /**
     * 缓存管理器
     *
     * @return EhCacheManager
     */
    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile(ehcacheConfig);
        return cacheManager;
    }

}
