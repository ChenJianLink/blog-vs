package cn.chenjianlink.blogv2.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

public class TimedLocalCache<K, V> extends WeakHashMap<K, TimedLocalCachedDataDTO<V>> {
    private final long timeout;

    /**
     * 默认5分钟过期
     */
    public TimedLocalCache() {
        timeout = TimeUnit.MINUTES.toSeconds(5);
    }

    public TimedLocalCache(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public TimedLocalCachedDataDTO<V> putCache(K key, V value) {
        TimedLocalCachedDataDTO<V> dataDTO = new TimedLocalCachedDataDTO<>(value);
        return super.put(key, dataDTO);
    }

    public V getCache(K key) {
        TimedLocalCachedDataDTO<V> dataDTO = super.get(key);
        if (dataDTO == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = dataDTO.getTime();
        long seconds = Duration.between(time, now).getSeconds();
        if (seconds > getTimeout()) {
            remove(key);
            return null;
        }
        return dataDTO.getData();
    }
}