package cache;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * guava Cache 缓存过期策略
 * 1、基于大小的移除:看字面意思就知道就是按照缓存的大小来移除，如果即将到达指定的大小，那就会把不常用的键值对从cache中移除。
 * 定义的方式一般为 CacheBuilder.maximumSize(long)，maximumSize指的是cache中的条目数
 * 注意：并不是完全到了指定的size系统才开始移除不常用的数据的，而是接近这个size的时候系统就会开始做移除的动作；
 * 2、基于时间的移除：guava提供了两个基于时间移除的方法
 * expireAfterAccess(long, TimeUnit) 这个方法是根据某个键值对最后一次访问之后多少时间后移除
 * expireAfterWrite(long, TimeUnit) 这个方法是根据某个键值对被创建或值被替换后多少时间移除
 * 3、基于引用的移除：
 * 这种移除方式主要是基于java的垃圾回收机制，根据键或者值的引用关系决定移除
 * <p>
 *
 * 主动移除
 * 单独移除用 Cache.invalidate(key)
 * 批量移除用 Cache.invalidateAll(keys)
 * 移除所有用 Cache.invalidateAll()
 */
@Slf4j
public class CacheNote {

    /**
     *
     */
    @SneakyThrows
    public static void main(String[] args) {

    }

}
