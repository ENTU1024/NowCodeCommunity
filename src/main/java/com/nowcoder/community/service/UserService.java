package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.LoginTicket;
import com.nowcoder.community.util.MailClient;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private MailClient mailClient;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    /*
    * 注册方法
    * */
    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        if (user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空！");
            return map;
        }

        //验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null ){
            map.put("usernameMsg","用户已经存在!");
            return map;
        }

        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
            map.put("emailMsg","邮箱已被注册!");
            return map;
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /*
    * 登录方法
    * */
    public Map<String, Object> login(String username, String password, long expiredSeconds){
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg", "不存在该用户");
            return map;
        }

        //验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","该账号为激活");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());

        return map;
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public int updateHeader(int id, String headerUrl){
        //这里有一个可以优化的地方，如果用户一直上传相同的图片怎么办，我不了解UUID的伪随机生成规律，但应该不能处理两个相同的图片
        if (StringUtils.isBlank(headerUrl)){
            return 0;
        }
/*        User user = userMapper.selectById(id);
        if (headerUrl.equals(user.getHeaderUrl())){
            return 1;
        }*/
        return userMapper.updateHeader(id, headerUrl);
    }


    public int updatePassword(int id,String forePassword, String password){
        User user = userMapper.selectById(id);
        String  foreMd5Password = CommunityUtil.md5(forePassword + user.getSalt());
        if (StringUtils.isBlank(password) || !user.getPassword().equals(foreMd5Password)){
            return 0;
        }
        String md5Password = CommunityUtil.md5(password + user.getSalt());
        return userMapper.updatePassword(id, md5Password);
    }
}
