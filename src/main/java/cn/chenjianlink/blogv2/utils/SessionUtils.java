package cn.chenjianlink.blogv2.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.stereotype.Component;


/**
 * session工具
 *
 * @author chenjian
 */
@Component
public class SessionUtils {

    @Autowired
    private EhCacheManagerFactoryBean ehCacheManagerFactoryBean;


    private String cacheName = "session";

    /**
     * 存储缓存，添加版本
     *
     * @param cacheKey   缓存键
     * @param cacheValue 缓存值
     * @param version    缓存版本
     */
    public void put(Object cacheKey, Object cacheValue, long version) {
        Cache cache = getCacheManager().getCache(cacheName);
        Element element = new Element(cacheKey, cacheValue, version);
        cache.put(element);
    }

    /**
     * 存储缓存，使用默认版本
     *
     * @param cacheKey   缓存键
     * @param cacheValue 缓存值
     */
    public void put(Object cacheKey, Object cacheValue) {
        put(cacheKey, cacheValue, 1L);
    }

    /**
     * 换取缓存对象
     *
     * @param cacheKey 缓存键
     * @return 返回指定缓存对象
     */
    public Object get(Object cacheKey) {
        Cache cache = getCacheManager().getCache(cacheName);
        Element element = cache.get(cacheKey);
        if (null == element) {
            return null;
        }
        return element.getObjectValue();
    }

    /**
     * 删除缓存
     *
     * @param cacheKey 缓存键
     * @return
     */
    public boolean remove(Object cacheKey) {
        Cache cache = getCacheManager().getCache(cacheName);
        return cache.remove(cacheKey);
    }

    /**
     * 获取EhCache管理者
     *
     * @return
     */
    public CacheManager getCacheManager() {
        return ehCacheManagerFactoryBean.getObject();
    }


}
