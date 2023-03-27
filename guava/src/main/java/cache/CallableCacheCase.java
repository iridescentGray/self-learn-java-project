package cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * Callable 为特定的key生成缓存规则，粒度极细，一个Key会有一个缓存
 * <p>
 * Guava CacheLoader和CallableCache的区别
 * 1.CacheLoader的定义比较固定，是针对整个cache定义的
 * 2.CallableCache更细粒度，在get(Key，CallableCache)的时候指定，每个key，都会有自己的缓存
 *
 * @author zjy
 * @date 2023/3/1
 */
@Slf4j
public class CallableCacheCase {

    public static void callableCacheCase() throws Exception {
        Cache<String, Integer> cache = CacheBuilder
                .newBuilder()
                .maximumSize(1000).build();
        //这里的call()方法，可以替换为将来的SQL查询等，可以省去将来的重复SQL查询
        Integer jerry = cache.get("jerry", new Callable<Integer>() {
            public Integer call() {
                System.out.println("build jerry cache");
                return 1;
            }
        });
        System.out.println("jerry value : " + jerry);
        //不会再输出第二次 "build jerry cache"
        System.out.println("jerry value : " + cache.getIfPresent("jerry"));
        System.out.println("jerry value : " + cache.getIfPresent("jerry"));


        //test的缓存不会干扰jerry的缓存，单独生成
        Integer test = cache.get("test", new Callable<Integer>() {
            public Integer call() {
                System.out.println("build test cache");
                return 2;
            }
        });
        System.out.println("test value : " + test);
        //不会再输出第二次 "build test cache"
        System.out.println("test value : " + cache.getIfPresent("test"));
        System.out.println("test value : " + cache.getIfPresent("test"));

    }

    @SneakyThrows
    public static void main(String[] args) {
        callableCacheCase();
    }

}
