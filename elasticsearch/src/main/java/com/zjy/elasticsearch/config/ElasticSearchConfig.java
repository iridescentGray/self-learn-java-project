package com.zjy.elasticsearch.config;

import cn.hutool.core.util.StrUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.zjy.elasticsearch.consts.ApplicationConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * es:
 * url: es-cn-zvp26zjly000se53r.elasticsearch.aliyuncs.com
 * username: elastic
 * password:
 * prefix: http
 * port: 9200
 *
 * @author zjy
 */
@Slf4j
public class ElasticSearchConfig {
    @Value("${es.url}")
    String url;
    @Value("${es.password}")
    String password;
    @Value("${es.username}")
    String username;
    @Value("${es.prefix}")
    String prefix;
    @Value("${es.port}")
    int port;

    @Bean
    @Deprecated
    public RestHighLevelClient restHighLevelClient() {
        log.info("连接es数据集群 url： {},prefix： {} ,port： {}", url, prefix, port);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        List<String> urls = StrUtil.split(url, ApplicationConst.BASE_SPLIT);
        HttpHost[] hosts = new HttpHost[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            hosts[i] = new HttpHost(urls.get(i), port, prefix);
        }
        RestClientBuilder builder = RestClient.builder(hosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }

    /**
     * 新版本
     * @return
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        log.info("连接es数据集群 url： {},prefix： {} ,port： {}", url, prefix, port);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        List<String> urls = StrUtil.split(url, ApplicationConst.BASE_SPLIT);
        HttpHost[] hosts = new HttpHost[urls.size()];
        int i = 0;
        for (String url : urls) {
            hosts[++i] = new HttpHost(url, port, prefix);
        }
        RestClient restClient = RestClient.builder(hosts)
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)).build();
        // 使用Jackson映射器创建传输层
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // 创建API客户端
        return new ElasticsearchClient(transport);
    }
}
