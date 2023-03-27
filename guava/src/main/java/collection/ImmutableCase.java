package collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * immutable 是指整个生命周期中，都是不可改变的
 * 1.线程安全的：immutable对象在多线程下安全，没有竞态条件
 * 2.可以被使用为一个常量，并且期望在未来也是保持不变的
 * 3.每个Guava immutable集合类的实现都拒绝null值
 *
 * @author zjy
 */
@Slf4j
public class ImmutableCase {


    /**
     * JDK 的Collections.unmodifiableList实现的不是真正的不可变集合
     * 当原始集合修改后，不可变集合也发生变化。
     * 强制修改不可变集合时会报错
     * 效率低：并发下修改带锁
     */
    public static void JDKImmutableCase() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        System.out.println(list);


        List<String> unmodifiableList = Collections.unmodifiableList(list);
        try {
            //修改不可变集合，会报错
            unmodifiableList.add("cc");
        } catch (Exception e) {
            log.error("error is", e);
        }
        //修改原集合，会导致不可变集合被修改
        list.add("baby");
        System.out.println("list add a item after list:" + list);
        System.out.println("unmodifiableList add a item after list:" + unmodifiableList);
    }

    /**
     * guava 的真正的不可变集合，当你不希望修改一个集合类，或者想做一个常量集合类的时候，使用immutable集合类就是一个最佳的编程实践。
     * 当原始集合修改后，不可变集合也发生变化。
     * 强制修改不可变集合时会报错
     * 效率低：并发下修改带锁
     */
    public static void guavaCreateImmutableCase() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println("list：" + list);

        ImmutableList<String> imlist = ImmutableList.copyOf(list);
        System.out.println("imlist：" + imlist);

        ImmutableList<String> imOflist = ImmutableList.of("peida", "jerry", "harry");
        System.out.println("imOflist：" + imOflist);

        ImmutableSortedSet<String> imSortList = ImmutableSortedSet.of("a", "b", "c", "a", "d", "b");
        System.out.println("imSortList：" + imSortList);

        //修改原集合，不会导致不可变集合被修改
        list.add("baby");
        System.out.println("list add a item after list:" + list);
        System.out.println("list add a item after imlist:" + imlist);
    }


    public static void main(String[] args) {
        System.out.println("----------------------JDKImmutableCase------------------------------");
        JDKImmutableCase();
        System.out.println("----------------------guavaImmutableCase------------------------------");
        guavaCreateImmutableCase();
    }
}
