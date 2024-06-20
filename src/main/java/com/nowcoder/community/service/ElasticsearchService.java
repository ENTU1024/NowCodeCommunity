package com.nowcoder.community.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.ml.Page;
import co.elastic.clients.util.ObjectBuilder;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchTemplate template;

    public void saveDiscussPost(DiscussPost post){
        discussPostRepository.save(post);
    }

    public void deleteDiscussPost(int id){
        discussPostRepository.deleteById(id);
    }

    public void searchDiscussPost(String keyword,int current, int limit) {
       /* NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder()
                .withQuery(QueryBuilders.termQueryAsQuery("title",keyword))
                .withQuery(QueryBuilders.termQueryAsQuery("content",keyword));

        Page<DiscussPost> discussPostPage = template.search*/
       /* MatchQuery query = QueryBuilders.matchQuery("title", "小米");
        Iterable<DiscussPost> discussPosts = discussPostRepository.search(query);
        items.forEach(System.out::println);*/
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("title").query(keyword)))
                .withPageable(PageRequest.of(current, limit))
                .build();
        SearchHits<DiscussPost> searchHit = template.search(query, DiscussPost.class);

    }
}
