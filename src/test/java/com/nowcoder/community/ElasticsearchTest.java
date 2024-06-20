package com.nowcoder.community;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.knn_search.KnnSearchQuery;
import co.elastic.clients.elasticsearch.core.search.Hit;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
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
       /* NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder()
                .withQuery(QueryBuilders.termQueryAsQuery("title",keyword))
                .withQuery(QueryBuilders.termQueryAsQuery("content",keyword));

        Page<DiscussPost> discussPostPage = template.search*/
       /* MatchQuery query = QueryBuilders.matchQuery("title", "小米");
        Iterable<DiscussPost> discussPosts = discussPostRepository.search(query);
        items.forEach(System.out::println);*/
        /*      NativeQuery query = NativeQuery.builder()
                        .withQuery(q -> q.match(m -> m.field("title").query(keyword)))
                        .withPageable(PageRequest.of(current, limit))
                        .build();
                SearchHits<DiscussPost> searchHit = template.search(query, DiscussPost.class);
        */

    @Test
    public void a(){
        String keyword = "互联网寒冬";
        int current = 0;
        int limit = 10;

        String field1 = "title";
        String field2 = "content";

        List<HighlightField> fieldList = new ArrayList<>();
        List<String> stringFieldList = new ArrayList<>();

        HighlightFieldParameters highlightParameters = HighlightFieldParameters.builder()
                .withPreTags("<span style='color:red'>")
                .withPostTags("</span>")
                .withFragmentSize(10) //高亮的片段长度(多少个几个字需要高亮，一般会设置的大一些，让匹配到的字段尽量都高亮)
                .withNumberOfFragments(1) //高亮片段的数量
                .build();

        HighlightField highlightField1 = new HighlightField(field1,highlightParameters);
        HighlightField highlightField2 = new HighlightField(field2,highlightParameters);

        stringFieldList.add(field1);
        stringFieldList.add(field2);
        fieldList.add(highlightField1);
        fieldList.add(highlightField2);

        Highlight highlight = new Highlight(fieldList);

        NativeQuery query = NativeQuery.builder()
                .withQuery(q->q.multiMatch(ma->ma.fields(stringFieldList).query(keyword)))
                .withSort( s -> s.field ( f->f.field("type").order(SortOrder.Desc)))
                .withSort( s -> s.field ( f->f.field("score").order(SortOrder.Desc)))
                .withSort( s -> s.field ( f->f.field("createTime").order(SortOrder.Desc)))
                .withPageable(PageRequest.of(current,limit))
                .withHighlightQuery( new HighlightQuery( new Highlight(fieldList), String.class) )
                .build();

        SearchHits<DiscussPost> searchHits = elasticsearchTemplate.search(query, DiscussPost.class);
        SearchPage<DiscussPost> page = SearchHitSupport.searchPageFor(searchHits,query.getPageable());
        Page<DiscussPost> posts = (Page) SearchHitSupport.unwrapSearchHits(page);
        for (DiscussPost post:  posts){
            System.out.println(post);
        }
    }
}