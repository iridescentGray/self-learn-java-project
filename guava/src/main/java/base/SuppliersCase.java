package base;

import com.google.common.base.Suppliers;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * Suppliers 是一个提供了多种方式对象供应的集合
 *
 * @author zjy
 * @date 2023/3/1
 */
@Slf4j
public class SuppliersCase {

    /**
     * memoize 可以延迟耗时的初始化操作到第一次get()时
     * 有些时候，后面如果不get，就省去了一次耗时操作（大对象创建，SQL查询），很有用
     */
    public static void memoizeCase() {
        Supplier<String> memoize = Suppliers.memoize(() -> {
            System.out.println("耗时操作执行一次");
            return "耗时的初始化对象操作";
        });
        if (true) {
            String s = memoize.get();
            //不论执行多少次Get，初始化只执行一次
            String s1 = memoize.get();
            String s2 = memoize.get();
            String s3 = memoize.get();
            System.out.println(s);
        } else {
            String s4 = memoize.get();
            String s5 = memoize.get();
            String s6 = memoize.get();
        }
    }


    public static void main(String[] args) {
        memoizeCase();
    }


}
