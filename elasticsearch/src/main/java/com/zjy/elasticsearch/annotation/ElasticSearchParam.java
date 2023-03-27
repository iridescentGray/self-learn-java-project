package com.zjy.elasticsearch.annotation;


import com.zjy.elasticsearch.enums.ElasticQueryType;
import com.zjy.elasticsearch.enums.ElasticSearchType;
import com.zjy.elasticsearch.enums.FormatRule;
import com.zjy.elasticsearch.support.ElasticSearchUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zjy
 * @version 1.0.0
 * @date 2021/12/7
 * @see ElasticSearchUtils  解析了@ElasticQuestion注解
 * @since 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchParam {
    /**
     * 查询字段名称
     */
    String value() default "";

    /**
     * 是否为多字段组合查询,如果是true，会根据value分割为多个查询条件
     */
    boolean isMultipart() default false;

    /**
     * 搜索模式
     */
    ElasticSearchType searchType() default ElasticSearchType.FILTER;

    /**
     * 查询方式
     */
    ElasticQueryType queryType() default ElasticQueryType.TERM;

    /**
     * 请求参数转换规则
     */
    FormatRule rule() default FormatRule.NONE;
}
