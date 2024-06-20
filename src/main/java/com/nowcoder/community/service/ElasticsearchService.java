package com.nowcoder.community.service;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.util.ObjectBuilder;
import com.nowcoder.community.dao.elasticsearch.DiscussPostRepository;
import com.nowcoder.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void saveDiscussPost(DiscussPost post){
        discussPostRepository.save(post);
    }

    public void deleteDiscussPost(int id){
        discussPostRepository.deleteById(id);
    }

    public Page<DiscussPost> searchDiscussPost(String keyword, int current, int limit) {
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
       return (Page) SearchHitSupport.unwrapSearchHits(page);
    }
}
