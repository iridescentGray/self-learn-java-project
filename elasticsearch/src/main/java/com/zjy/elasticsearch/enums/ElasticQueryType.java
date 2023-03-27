package com.zjy.elasticsearch.enums;

/**
 * ES 查询方式枚举
 *
 * @author zjy
 */
public enum ElasticQueryType {

    /**
     * 精确查询
     */
    TERM,
    /**
     * 大于等于
     */
    GE,
    /**
     * 小于等于
     */
    LE,
    /**
     * 批量精确匹配
     */
    TERMS,
    /**
     * 分词匹配
     */
    MATCH,
    /**
     * 查询时的范围值
     */
    RAND,
    /**
     * 模糊匹配
     */
    WILDCARD
}
