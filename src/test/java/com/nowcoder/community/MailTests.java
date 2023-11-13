package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    MailClient mailClient;

    @Autowired
    SensitiveFilter sensitiveFilter;

    @Test
    public void testTextMail ( ){
        mailClient.sendMail("1590560554@qq.com","test","这是一封测试邮件");
    }

    @Test
    public void testText(){
        //String string = "草快来@赌#博￥，狠狠地#嫖#娼bfabc，嫖娼";
        String string = "草";
        string = sensitiveFilter.filter(string);
        System.out.println(string);
    }
}
