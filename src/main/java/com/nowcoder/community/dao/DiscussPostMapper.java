package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //userId: offset:每一页起始行的行号 limit:每一页最多显示多少数据
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@param注解用于取别名
    //但如果只有一个参数，并且在<if>里使用，则必须加上别名
    //一个可能有多少页
    int selectDiscussPostRows (@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);
    int updateCommentCount(int id, int commentCount);
}
