package com.zjy.util.jdk;

import cn.hutool.core.util.StrUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 行为工具
 *
 * @author zjy
 * @version 1.0.0
 * @date 2021-12-23
 * @since 1.0.0
 */
public final class BehaviorUtils {

    /**
     * 行为工具对外暴露的实例
     */
    public static final BehaviorUtils INSTANCE = new BehaviorUtils();

    private BehaviorUtils() {
        super();
    }

    /**
     * 如果条件为true，则调用{@link Consumer#accept（T）}。
     *
     * @param condition 条件.
     * @param value     被消费者.
     * @param consumer  消费者
     * @param <T>       值类型
     * @return this.
     */
    public <T> BehaviorUtils acceptIfCondition(boolean condition, T value, Consumer<T> consumer) {
        if (condition) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * 修改目标值，如果condition=ture
     *
     * @param condition 条件
     * @param supplier  被消费的值提供者.
     * @param consumer  修改消费者
     * @param <T>       值类型
     * @return this     当前对象的单例
     */
    public <T> BehaviorUtils modifyIfCondition(boolean condition, Supplier<T> supplier, Consumer<T> consumer) {
        if (condition) {
            consumer.accept(supplier.get());
        }
        return this;
    }

    /**
     * 如果{@link Consumer#accept（Object）}不为null，则使用该值调用它。
     *
     * @param value    被消费者.
     * @param consumer 消费者
     * @param <T>      值类型
     * @return this.   当前对象的单例
     */
    public <T> BehaviorUtils acceptIfNotNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * 如果{@link Consumer#accept（Object）}不为null或空，则使用该值调用它。
     *
     * @param value    被消费者.
     * @param consumer 消费者
     * @return this.   当前对象的单例
     */
    public BehaviorUtils acceptIfHasText(String value, Consumer<String> consumer) {
        if (StrUtil.isNotBlank(value)) {
            consumer.accept(value);
        }
        return this;
    }

}
