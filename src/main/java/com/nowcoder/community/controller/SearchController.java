package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant{

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    private static CommunityConstant communityConstant;

    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public  String search(String keyword, Page page, Model model) {
        //搜索帖子
        org.springframework.data.domain.Page<DiscussPost> searchDiscussPost =
                elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        List<Map<String , Object>> discussPosts = new ArrayList<>();
        if (searchDiscussPost != null){
            for (DiscussPost discussPost : searchDiscussPost){
                Map<String , Object> map = new HashMap<>();
                //帖子
                map.put("post",discussPost);
                //作者
                map.put("user",userService.findUserById(discussPost.getUserId()));
                //点赞数量
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPost.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("keyword",keyword);

        page.setPath("/search?keyword=" + keyword);
        page.setRows(searchDiscussPost == null ? 0 : (int) searchDiscussPost.getTotalElements());

        return "/site/search";
    }
}
