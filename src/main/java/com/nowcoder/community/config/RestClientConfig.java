package com.nowcoder.community.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;

@Configuration
public class RestClientConfig {

        //注入IOC容器
        @Bean
        public ElasticsearchClient elasticsearchClient(){
            RestClient client = RestClient.builder(new HttpHost("localhost", 9200,"http")).build();
            ElasticsearchTransport transport = new RestClientTransport(client,new JacksonJsonpMapper());
            return new ElasticsearchClient(transport);
        }
}