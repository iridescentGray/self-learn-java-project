package com.zjy.elasticsearch.enums;

/**
 * ES 搜索模式枚举
 *
 * @author zjy
 */
public enum ElasticSearchType {
    /**
     * 必须 匹配，但它以不评分、过滤模式来进行
     */
    FILTER,
    /**
     * 如果满足这些语句中的任意语句，将增加 _score ，否则，无任何影响
     */
    SHOULD,
    /**
     * 必须 匹配这些条件
     */
    MUST,
    /**
     * 必须不 匹配这些条件
     */
    MUST_NOT
}
