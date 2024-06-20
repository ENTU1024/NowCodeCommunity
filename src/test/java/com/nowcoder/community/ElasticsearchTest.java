package com.nowcoder.community;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.knn_search.KnnSearchQuery;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.ml.Page;
import co.elastic.clients.util.ObjectBuilder;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import jakarta.annotation.PostConstruct;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.elasticsearch.client.RestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Dictionary;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
@EnableElasticsearchRepositories(basePackages = "com.community.dao.elasticsearch")
public class ElasticsearchTest {


    @Autowired
    private  ElasticsearchClient elasticsearchClient;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations ;

    @Test
    public void testInsert(){
        elasticsearchOperations.save(discussPostMapper.selectDiscussPostById(241));
        elasticsearchOperations.save(discussPostMapper.selectDiscussPostById(242));
        elasticsearchOperations.save(discussPostMapper.selectDiscussPostById(243));
    }

    @Test
    public void testInsertList(){
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133,0,100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134,0,100));
    }

    @Test
    public void testUpdate(){
        DiscussPost post = discussPostMapper.selectDiscussPostById(231);
        post.setContent("我是王江红，666");
        discussPostRepository.save(post);
    }

    @Test
    public void testDelete(){
        // discussPostRepository.deleteById(231);
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchByRepository() {

        /*Query query =  new CriteriaQuery(new Criteria("title").is("互联网寒冬").or("content").is("互联网寒冬"));
        SearchHitsIterator<DiscussPost> hitsIterator = elasticsearchOperations.searchForStream(query, DiscussPost.class);
        for (SearchHitsIterator<DiscussPost> it = hitsIterator; it.hasNext(); ) {
            SearchHit<DiscussPost> hit = it.next();
            System.out.println(hit);
        }*/
        //discussPostRepository.findAll();
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("title").query("人")))
                .withQuery(q -> q.match(m -> m.field("content").query("人")))
                //.withPageable(PageRequest.of(0, 1))
                .build();
        SearchHits<DiscussPost> searchHits = elasticsearchTemplate.search(query, DiscussPost.class);
        System.out.println("-------------------");
        while (searchHits.iterator().hasNext()){
            SearchHit<DiscussPost> searchHit = searchHits.iterator().next();
            System.out.println(searchHit);
        }
    }

    @Test
    public void testSearchByOne() {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(t -> t.field("title").query("人")))
                .build();
        SearchHits<DiscussPost> searchHits = elasticsearchTemplate.search(query, DiscussPost.class);
        System.out.println("-------------------");
        while (searchHits.iterator().hasNext()) {
            SearchHit<DiscussPost> searchHit = searchHits.iterator().next();
            System.out.println("+++++++++++++++++++++++=");
            System.out.println(searchHit.toString());
            System.out.println("+++++++++++++++++++++++=");
        }
    }

    //SearchResponse<DiscussPost> searchResponse = elasticsearchClient.search(query,DiscussPost.class);
}