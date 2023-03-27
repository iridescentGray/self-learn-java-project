package com.zjy.elasticsearch.service;

import com.zjy.elasticsearch.domain.ElasticQueryParam;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;

/**
 * 岗位查询 ES 数据
 *
 * @author zjy
 */
@Deprecated
public interface ElasticSearchService {

    /**
     * 查询 ES 数据-分页查询
     *
     * @param queryParams     查询参数
     * @param index           索引名
     * @param orderDescFields 排序字段
     * @param searchFields    需要搜索的字段
     * @param pageNum         当前是第几页
     * @param pageSize        每页大小
     * @return 搜索结果
     * @throws IOException 查询 Es 可能连接出现异常
     */
    SearchResponse pageSearch(List<ElasticQueryParam> queryParams, String index, String[] searchFields, List<String> orderDescFields, int pageNum, int pageSize) throws IOException;

}
