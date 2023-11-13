package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {
    //查询用户的所有会话，每个会话只返回最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);
    //查询当前用户的会话数量（和多少人有私信）
    int selectConversationCount(int userId);
    //获取用户的某个会话（私信）的所有信息
    List<Message> selectLetters(String conversationId, int offset, int limit);
    //获取用户的某个会话（私信）的数量
    int selectLetterCount(String conversationId);
    //查询未读私信的数量
    int selectLetterUnreadCount(int userId, String conversationId);

    //传入消息
    int insertMessage(Message message);

    //更新一个会话内一条或多条未读消息
    int updateStatus(List<Integer> ids,int status);
}
