package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.LoginTicket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder holder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取凭证
        String  ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null){
        //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //查询凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //根据凭证查找用户
                User user = userService.findUserById(loginTicket.getUserId());
                holder.setUser(user);
            }
            //泪的教训，这里不能写else，因为第一次登录（status为1 ），他就通过不了
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = holder.getUser();
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        holder.clear();
    }
}
