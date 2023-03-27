package cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * LoadingCache 是一个逐步加载的缓存
 * 每次get，会先判断缓存是否存在
 * 如果存在，直接从缓存中获取
 * 如果不存在，再从CacheLoader计算并获取值，然后添加到缓存中
 * <p>
 * Guava CacheLoader和CallableCache的区别
 * 1.CacheLoader的定义比较固定，是针对整个cache定义的
 * 2.CallableCache更细粒度，在get(Key，CallableCache)的时候指定，每个key，都会有自己的缓存
 *
 * @author zjy
 * @date 2023/3/1
 */
@Slf4j
public class LoadingCacheCase {

    /**
     * 普通的CacheLoader
     */
    static CacheLoader<Long, Long> demoLongValueCacheLoader;

    /**
     * 异步的的CacheLoader
     */
    static CacheLoader<Long, Long> demoAsyncLongValueCacheLoader;

    /**
     * 根据Function构建的CacheLoader
     */
    static CacheLoader<Long, Long> functionCacheLoader;

    /**
     * 根据Supplier构建的CacheLoader
     */
    static CacheLoader<Object, Long> supplierCacheLoader;

    /**
     * 执行时，100%抛出异常的CacheLoader
     */
    static CacheLoader<Long, Long> demoErrorLongValueCacheLoader;

    static {
        demoLongValueCacheLoader = new CacheLoader<Long, Long>() {
            @Override
            public Long load(Long key) {
                log.info("demoLongValueCacheLoader get not from cache param is:{}", key);
                return key;
            }
        };

        demoAsyncLongValueCacheLoader = buildAsyncCacheLoader(new CacheLoader<Long, Long>() {
            @Override
            public Long load(Long key) {
                log.info("demoLongValueCacheLoader get not from cache param is:{}", key);
                return key;
            }
        });

        functionCacheLoader = CacheLoader.from((key) -> key);

        supplierCacheLoader = CacheLoader.from(() -> {
                    return 1L;
                }
        );

        demoErrorLongValueCacheLoader = new CacheLoader<Long, Long>() {
            @Override
            public Long load(Long key) {
                log.info("demoErrorLongValueCacheLoader get not from cache param is:{}", key);
                if (true) {
                    throw new RuntimeException();
                }
                return key;
            }
        };

    }

    /**
     * 把CacheLoader封装成asyncCacheLoader
     *
     * @param loader 普通CacheLoader
     * @return 异步CacheLoader
     */
    public static <K, V> CacheLoader<K, V> buildAsyncCacheLoader(CacheLoader<K, V> loader) {
        return CacheLoader.asyncReloading(loader, Executors.newCachedThreadPool());
    }

    /**
     * 根据 过期时间 和CacheLoader 生成LoadingCache
     *
     * @param cacheLoader CacheLoader
     * @param duration    过期时间
     * @return LoadingCache
     */
    public static <K, V> LoadingCache<K, V> buildLoadingCache(Duration duration, CacheLoader<K, V> cacheLoader) {
        return CacheBuilder.newBuilder()
                // 只阻塞当前数据加载线程，其他线程返回旧值
                .refreshAfterWrite(duration)
                // 通过 asyncReloading 实现全异步加载，包括 refreshAfterWrite 被阻塞的加载线程
                .build(cacheLoader);

    }


    public static void main(String[] args) {
        LoadingCache<Long, Long> longValueLoadingCache = buildLoadingCache(Duration.ofMinutes(1L), demoLongValueCacheLoader);
        try {
            log.info("longValueLoadingCache get {}", longValueLoadingCache.get(1L));
        } catch (ExecutionException e) {
            log.error("longValueLoadingCache get error ", e);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //getUnchecked和get方法的区别是，不用强制检查ExecutionException 这个异常
        log.info("longValueLoadingCache getUnchecked {}", longValueLoadingCache.getUnchecked(1L));

        System.out.println();
        System.out.println("-------------------------------------------------------------------------");


        LoadingCache<Long, Long> asyncLongValueLoadingCache = buildLoadingCache(Duration.ofMinutes(1L), demoAsyncLongValueCacheLoader);
        try {
            log.info("asyncLongValueLoadingCache get {}", asyncLongValueLoadingCache.get(1L));
        } catch (ExecutionException e) {
            log.error("asyncLongValueLoadingCache get error ", e);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //getUnchecked和get方法的区别是，不用强制检查ExecutionException 这个异常
        log.info("asyncLongValueLoadingCache getUnchecked {}", asyncLongValueLoadingCache.getUnchecked(1L));

        System.out.println();
        System.out.println("-------------------------------------------------------------------------");


        CacheLoader<Long, Long> asyncErrorLongValueCacheLoader = buildAsyncCacheLoader(demoErrorLongValueCacheLoader);
        LoadingCache<Long, Long> asyncErrorLongValueLoadingCache = buildLoadingCache(Duration.ofMinutes(1L), asyncErrorLongValueCacheLoader);
        try {
            log.info("asyncErrorLongValueLoadingCache get {}", asyncErrorLongValueLoadingCache.get(1L));
        } catch (ExecutionException e) {
            log.error("asyncErrorLongValueLoadingCache get error ", e);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //getUnchecked和get方法的区别是，不用强制检查ExecutionException 这个异常
        log.info("asyncErrorLongValueLoadingCache getUnchecked {}", asyncErrorLongValueLoadingCache.getUnchecked(1L));


    }


}
