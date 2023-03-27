package com.zjy.elasticsearch.domain;

import com.zjy.elasticsearch.enums.ElasticQueryType;
import com.zjy.elasticsearch.enums.ElasticSearchType;
import lombok.Data;

import java.util.List;

/**
 * @author zjy
 */
@Data
public class ElasticQueryParam {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 返回查询 开始值
     */
    private Object ge;
    /**
     * 范围查询 结束值
     */
    private Object le;
    /**
     * match term 等单一查询的值
     */
    private Object value;
    /**
     * 批量 terms
     */
    private List<?> terms;
    /**
     * 查询方式
     */
    private ElasticQueryType queryType;
    /**
     * 搜索模式
     */
    private ElasticSearchType searchType;
}
