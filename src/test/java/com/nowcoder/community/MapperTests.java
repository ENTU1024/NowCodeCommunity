package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.LoginTicket;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void selectTest() {
        User user = userMapper.selectById(152);
        System.out.println(user);
    }


    @Test
    public void selectDiscussTest(){
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for(DiscussPost post : list) {
            System.out.println(post);
        }
    }
    @Test
    public void updateTicketTest(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setStatus(1);
        loginTicket.setTicket("asdfsfsdf");
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 *10 ));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void selectByTicketTest(){
        String st= "asdfsfsdf";
        LoginTicket loginTicket = loginTicketMapper.selectByTicket(st);
        System.out.println(loginTicket);
    }
    @Test
    public void updateStatusTicketTest(){
        loginTicketMapper.updateStatus("asdfsfsdf", 0);

    }

    @Test
    public void messageMapperTest(){
 /*      List<Message> list = messageMapper.selectConversations(111,0,20);
       for (Message message : list){
           System.out.println(message);
       }

       int count  = messageMapper.selectConversationCount(111);
        System.out.println(count);*/

      /* List<Message> list1 =  messageMapper.selectLetters("111_112",0,10);
        for (Message message1 : list1){
            System.out.println(message1);
        }
       int count1 = messageMapper.selectLetterCount("111_112");
        System.out.println(count1);
*/
       int count2 =  messageMapper.selectLetterUnreadCount(111,null);
        System.out.println(count2);

    }
}
