package com.zjy.elasticsearch.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PageUtil;
import com.zjy.elasticsearch.domain.ElasticQueryParam;
import com.zjy.elasticsearch.service.ElasticSearchService;
import com.zjy.elasticsearch.support.ElasticSearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author zjy
 * @version 1.0.0
 * @date 2021/8/26
 * @since 1.0.0
 */
@Deprecated
@Slf4j
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public SearchResponse pageSearch(List<ElasticQueryParam> queryParams, String index, String[] searchFields, List<String> orderDescFields, int pageNum, int pageSize) throws IOException {
        // 构建search source
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.count("count").field("id"));
        searchSourceBuilder.size(pageSize);
        PageUtil.setOneAsFirstPageNo();
        searchSourceBuilder.from(PageUtil.getStart(pageNum, pageSize));
        //构建查询参数
        searchSourceBuilder.query(ElasticSearchUtils.buildQueryBuilderByElasticQueryParam(queryParams));
        searchSourceBuilder.fetchSource(searchFields, null);
        if (ObjectUtil.isNotEmpty(orderDescFields)) {
            for (String orderDescField : orderDescFields) {
                searchSourceBuilder.sort(orderDescField, SortOrder.DESC);
            }
        }
        log.info("Es 查询语句 ===>{}", searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

}
