package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.LoginTicket;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void selectTest(){
        User user = userMapper.selectById(101);
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
}
