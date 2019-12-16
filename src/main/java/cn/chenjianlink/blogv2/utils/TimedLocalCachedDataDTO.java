package cn.chenjianlink.blogv2.utils;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 当前时间
 *
 * @param <V>
 * @author chenjian
 */
@Getter
@Setter
public class TimedLocalCachedDataDTO<V> {
    private final LocalDateTime time;
    private final V data;

    public TimedLocalCachedDataDTO(V data) {
        this.time = LocalDateTime.now();
        this.data = data;
    }
}
