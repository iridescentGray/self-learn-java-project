package collection;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

/**
 * Multimap 是一个用来把Map<String, List<Integer>> 简化为 Multimap<String, Integer>的Map
 *
 * @author zjy
 */
@Slf4j
public class MultimapCase {

    private static void hashMultimapCase() {
        Multimap<String, Integer> keyMappingInteger = HashMultimap.create();
        for (int i = 10; i < 20; i++) {
            keyMappingInteger.put("key", i);
            keyMappingInteger.put("key", i);
        }
        System.out.println(StrUtil.format("size:{}", keyMappingInteger.size()));
        System.out.println(StrUtil.format("keySet:{}", keyMappingInteger.keySet()));
        System.out.println(StrUtil.format("getByKey:{}", keyMappingInteger.get("key")));
        System.out.println(StrUtil.format("containsValue:{}", keyMappingInteger.containsValue(9)));
        System.out.println(StrUtil.format("containsValue:{}", keyMappingInteger.containsValue(11)));
        System.out.println(StrUtil.format("containsEntry :{}", keyMappingInteger.containsEntry("key", 11)));
        System.out.println(StrUtil.format("containsEntry:{}", keyMappingInteger.containsEntry("?", 11)));

    }

    public static void arrayListMultimapCase() {
        Multimap<String, Integer> keyMappingInteger = ArrayListMultimap.create();
        for (int i = 10; i < 20; i++) {
            keyMappingInteger.put("key", i);
            keyMappingInteger.put("key", i);
        }

        System.out.println(StrUtil.format("size:{}", keyMappingInteger.size()));
        System.out.println(StrUtil.format("keySet:{}", keyMappingInteger.keySet()));
        System.out.println(StrUtil.format("getByKey:{}", keyMappingInteger.get("key")));
        System.out.println(StrUtil.format("containsValue:{}", keyMappingInteger.containsValue(9)));
        System.out.println(StrUtil.format("containsValue:{}", keyMappingInteger.containsValue(11)));
        System.out.println(StrUtil.format("containsEntry :{}", keyMappingInteger.containsEntry("key", 11)));
        System.out.println(StrUtil.format("containsEntry:{}", keyMappingInteger.containsEntry("?", 11)));

    }


    public static void main(String[] args) {
        hashMultimapCase();
        System.out.println("----------------------------------------------------");
        arrayListMultimapCase();
        System.out.println("----------------------------------------------------");
    }
}
