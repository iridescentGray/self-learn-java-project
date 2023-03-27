package collection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Bimap 是一个双向Map
 *
 * @author zjy
 */
@Slf4j
public class BimapCase {


    /**
     * 简单的反转Map
     * 有以下两个不足：
     * 1.如何处理重复的value的情况。不考虑的话，反转的时候就会出现覆盖的情况.
     * 2.如果在反转的map中增加一个新的key，倒转前的map是否需要更新一个值呢?
     */
    public static void simpleInverseMapCase() {
        Map<Integer, String> logfileMap = Maps.newHashMap();
        logfileMap.put(1, "a.log");
        logfileMap.put(2, "b.log");
        logfileMap.put(3, "c.log");
        log.info("logfileMao:{}", logfileMap);

        Map<String, Integer> logfileInverseMap = Maps.newHashMap();
        logfileInverseMap = getInverseMap(logfileMap);
        log.info("logfileInverseMap:{}", logfileInverseMap);
    }

    /**
     * 逆转Map的key和value
     *
     * @param <S>
     * @param <T>
     * @param map
     * @return
     */
    private static <S, T> Map<T, S> getInverseMap(Map<S, T> map) {
        Map<T, S> inverseMap = new HashMap<T, S>();
        for (Map.Entry<S, T> entry : map.entrySet()) {
            inverseMap.put(entry.getValue(), entry.getKey());
        }
        return inverseMap;
    }


    /**
     * 1.inverse()方法返回的bimap由与原Map引用相同，对其中一项的任何更改都将显示在另一项中。
     * 2.不能存放相同的value，会报错java.lang.IllegalArgumentException: value already present: ,除非使用forcePut方法
     */
    public static void bimapCase() {
        BiMap<Integer, String> logfileMap = HashBiMap.create();
        logfileMap.put(1, "a.log");
        logfileMap.put(2, "b.log");
        logfileMap.put(3, "c.log");
        BiMap<String, Integer> filelogMap = logfileMap.inverse();
        log.info("bimapCase:{}", logfileMap);
        log.info("inverse bimapCase:{}", filelogMap);

        logfileMap.put(4, "d.log");
        log.info("bimapCase after put:{}", logfileMap);
        log.info("inverse bimapCase after put:{}", filelogMap);

        try {
            logfileMap.put(5, "d.log");
        } catch (Exception e) {
            log.error("put(5,\"d.log\") error is ", e);
        }
    }

    public static void main(String[] args) {
        simpleInverseMapCase();
        bimapCase();
    }
}
